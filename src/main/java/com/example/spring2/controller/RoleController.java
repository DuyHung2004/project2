package com.example.spring2.controller;

import com.example.spring2.dto.reponse.PermissionReponse;
import com.example.spring2.dto.reponse.RoleReponse;
import com.example.spring2.dto.request.ApiResponse;
import com.example.spring2.dto.request.PermissionsRequest;
import com.example.spring2.dto.request.RoleRequest;
import com.example.spring2.service.PermissionService;
import com.example.spring2.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;
    @PostMapping
    ApiResponse<RoleReponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleReponse>builder()
                .result(roleService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleReponse>> getAll(){
        return ApiResponse.<List<RoleReponse>>builder()
                .result(roleService.getAll())
                .build();
    }
    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String roleid){
        roleService.delete(roleid);
        return ApiResponse.<Void>builder().build();
    }
}
