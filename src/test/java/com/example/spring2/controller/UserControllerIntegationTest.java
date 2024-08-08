package com.example.spring2.controller;

import com.example.spring2.dto.reponse.UserResponse;
import com.example.spring2.dto.request.UserCreationRequest;
import com.example.spring2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Date;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER= new MySQLContainer<>("mysql:latest");


    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", ()->"com.mysql.cj.jdbc.Driver");

        registry.add("spring.jpa.hibernate,ddl-auto", ()->"update");
    }


    @Autowired
    private MockMvc mockMvc;

    private UserCreationRequest request;
    private UserResponse userResponse;
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
    }


    @Test

    void createUser_validRequest_success() throws Exception {

        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content= objectMapper.writeValueAsString(request);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("result.username").value("johnasdf")
                );


    }


}
