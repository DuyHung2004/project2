package com.example.spring2.controller;

import com.example.spring2.dto.reponse.UserResponse;
import com.example.spring2.dto.request.ApiResponse;
import com.example.spring2.dto.request.UserCreationRequest;
import com.example.spring2.dto.request.UserUpdateRequest;
import com.example.spring2.entity.User;
import com.example.spring2.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/users")
@Slf4j
public class UserController {
     UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        log.info("aaaa");
        return apiResponse;
    }
    @GetMapping
    ApiResponse<List<UserResponse>> getuser(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }
    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyinfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getmyinfo())
                .build();
    }
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();

    }
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "Da xoa thanh cong";
    }
}
