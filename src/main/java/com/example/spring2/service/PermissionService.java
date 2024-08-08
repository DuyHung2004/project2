package com.example.spring2.service;

import com.example.spring2.dto.reponse.PermissionReponse;
import com.example.spring2.dto.request.PermissionsRequest;
import com.example.spring2.entity.Permission;
import com.example.spring2.mapper.PermissionMapper;
import com.example.spring2.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public PermissionReponse create(PermissionsRequest request){
        Permission permission= permissionMapper.toPermission(request);
        log.info("PermissionsRequest: {}", request);
        log.info("PermissionsRequest: {}", permission);
        return permissionMapper.toPermissionReponse(permissionRepository.save(permission));
    }
    public List<PermissionReponse> getAll(){
        var permission= permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toPermissionReponse).toList();
    }
    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
