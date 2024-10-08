package com.example.spring2.configuration;

import com.example.spring2.entity.Permission;
import com.example.spring2.entity.User;
import com.example.spring2.entity.Role;
import com.example.spring2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    @Bean
    @ConditionalOnProperty(prefix = "spring", value = "spring.datasource.driverClassName"
    , havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository){
        log.info("bbbb");
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                Role role= new Role("admin","a",new HashSet<Permission>());
                var roles= new HashSet<Role>();
                roles.add(role);
                User user= User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with....");
            }
        };
    }
}
