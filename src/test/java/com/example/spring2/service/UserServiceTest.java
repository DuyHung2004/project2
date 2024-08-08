package com.example.spring2.service;

import com.example.spring2.dto.reponse.UserResponse;
import com.example.spring2.dto.request.UserCreationRequest;
import com.example.spring2.entity.User;
import com.example.spring2.exception.AppException;
import com.example.spring2.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static   org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    UserService userService;
    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private Date date;

    @BeforeEach
    void initData(){
        String str = "2004-09-11";
        date= Date.valueOf(str);
        request= UserCreationRequest.builder()
                .username("johnasdf")
                .firstName("JOHN")
                .lastName("Doe")
                .password("12345678")
                .ngaysinh(date)
                .build();

        userResponse=UserResponse.builder()
                .id("zjdsfnsxjcdf")
                .username("johnasdf")
                .firstName("JOHN")
                .lastName("Doe")
                .ngaysinh(date)
                .build();
        user= User.builder()
                .id("zjdsfnsxjcdf")
                .username("johnasdf")
                .firstName("JOHN")
                .lastName("Doe")
                .password("12345678")
                .ngaysinh(date)
                .build();
    }

    @Test
    void createUser_validRequest_success(){

        when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
        when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        var response= userService.createUser(request);

        Assertions.assertThat(response.getId()).isEqualTo("zjdsfnsxjcdf");
        Assertions.assertThat(response.getUsername()).isEqualTo("johnasdf");

    }
    @Test
    void createUser_userExisted_fail(){

        when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);
        when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        var exception= assertThrows(AppException.class,()-> userService.createUser(request));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }
    @Test
    @WithMockUser(username = "john" )
    void getmyinfo_valid_success(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response= userService.getmyinfo();

        Assertions.assertThat(response.getUsername()).isEqualTo("johnasdf");
        Assertions.assertThat(response.getId()).isEqualTo("zjdsfnsxjcdf");
    }

    @Test
    @WithMockUser(username = "john" )
    void getmyinfo_userNotFound_error(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        var exception= assertThrows(AppException.class,()-> userService.getmyinfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

}
