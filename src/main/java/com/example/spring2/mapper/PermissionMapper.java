package com.example.spring2.mapper;

import com.example.spring2.dto.reponse.PermissionReponse;
import com.example.spring2.dto.reponse.UserResponse;
import com.example.spring2.dto.request.PermissionsRequest;
import com.example.spring2.dto.request.UserUpdateRequest;
import com.example.spring2.entity.Permission;
import com.example.spring2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionsRequest request);
    PermissionReponse toPermissionReponse(Permission permission);
}
