package com.example.spring2.mapper;

import com.example.spring2.dto.reponse.RoleReponse;
import com.example.spring2.dto.request.RoleRequest;
import com.example.spring2.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    @Mapping(target = "permissions", source = "permissions")
    RoleReponse toRoleReponse(Role role);
}
