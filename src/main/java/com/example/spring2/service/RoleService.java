package com.example.spring2.service;

import com.example.spring2.dto.reponse.RoleReponse;
import com.example.spring2.dto.request.RoleRequest;
import com.example.spring2.entity.Role;
import com.example.spring2.mapper.RoleMapper;
import com.example.spring2.repository.PermissionRepository;
import com.example.spring2.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    public RoleReponse create(RoleRequest request){
        log.info("a: {}",request);
        Role role= roleMapper.toRole(request);
        log.info("a: {}",role);
        var permissions= permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role= roleRepository.save(role);
        log.info("a: {}",permissions);
        log.info("a: {}",role.getPermissions());
        return roleMapper.toRoleReponse(role);
    }
    public List<RoleReponse> getAll(){
        var roles= roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleReponse).toList();

    }
    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
