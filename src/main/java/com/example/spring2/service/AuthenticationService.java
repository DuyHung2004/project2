package com.example.spring2.service;

import com.example.spring2.dto.reponse.AuthenticationResponse;
import com.example.spring2.dto.reponse.IntrospectReponse;
import com.example.spring2.dto.request.AuthendicationRequest;
import com.example.spring2.dto.request.IntrospectRequest;
import com.example.spring2.dto.request.LogoutRequest;
import com.example.spring2.dto.request.RefreshRequest;
import com.example.spring2.entity.InvalidatedToken;
import com.example.spring2.entity.User;
import com.example.spring2.exception.AppException;
import com.example.spring2.exception.ErrorCode;
import com.example.spring2.repository.InvalidatedTokenRepository;
import com.example.spring2.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository tokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;
    public IntrospectReponse introspectReponse(IntrospectRequest request) throws JOSEException, ParseException {
        var token= request.getToken();
        boolean inValid= true;
        try {
            verifyToken(token,false);
        }catch (AppException e){
            inValid= false;
        }
        return IntrospectReponse.builder()
                .valid(inValid)
                .build();
    }
    public AuthenticationResponse authentica(AuthendicationRequest request){
        var user= userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
        boolean authenticated= passwordEncoder.matches(request.getPassword(),user.getPassword());
        if (!authenticated){
            throw  new AppException(ErrorCode.UNAUTHENTICATED);

        }
        var token= generateToken(user);
        return  AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit= signToken.getJWTClaimsSet().getJWTID();
            Date expiryTimeUtil = signToken.getJWTClaimsSet().getExpirationTime();
            java.sql.Date expiryTime = new java.sql.Date(expiryTimeUtil.getTime());
            InvalidatedToken invalidatedToken= InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            log.info("Saving invalidated token: {}", invalidatedToken);
            tokenRepository.save(invalidatedToken);
        } catch (AppException exception){

        }
    }
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        log.info("Verifying token: {}", token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT= SignedJWT.parse(token);
        Date expityTime= (isRefresh)
                ? new Date( signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified= signedJWT.verify(verifier);
        log.info("Token verified: {}", verified);
        if (!(verified  && expityTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
    String generateToken(User user){
        JWSHeader header= new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet= new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("devteria.com")
                .issueTime(new java.util.Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            Logger log = null;
            log.error("cannot create token",e);
            throw new RuntimeException(e);
        }
    }
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT= verifyToken(request.getToken(),true);
        var jit= signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime= signJWT.getJWTClaimsSet().getExpirationTime();
        java.sql.Date sqlExpiryTime = new java.sql.Date(expiryTime.getTime());
        InvalidatedToken invalidatedToken= InvalidatedToken.builder()
                .id(jit)
                .expiryTime(sqlExpiryTime)
                .build();

        tokenRepository.save(invalidatedToken);
        var username= signJWT.getJWTClaimsSet().getSubject();
        var user= userRepository.findByUsername(username).orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        var token= generateToken(user);
        return  AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
}

