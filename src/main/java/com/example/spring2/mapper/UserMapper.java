package com.example.spring2.mapper;

import com.example.spring2.dto.reponse.UserResponse;
import com.example.spring2.dto.request.UserCreationRequest;
import com.example.spring2.dto.request.UserUpdateRequest;
import com.example.spring2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
//    @Mapping(source = "",target = "")
    @Mapping(target = "roles",source = "roles")
    UserResponse toUserResponse(User user);
}
