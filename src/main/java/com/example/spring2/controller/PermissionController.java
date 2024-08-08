package com.example.spring2.controller;

import com.example.spring2.dto.reponse.PermissionReponse;
import com.example.spring2.dto.request.ApiResponse;
import com.example.spring2.dto.request.PermissionsRequest;
import com.example.spring2.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/permissions")
@Slf4j
public class PermissionController {
    PermissionService permissionService;
    @PostMapping
    ApiResponse<PermissionReponse> create(@RequestBody PermissionsRequest request){
        log.info("PermissionsRequest: {}", request);
        return ApiResponse.<PermissionReponse>builder()
                .result(permissionService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<PermissionReponse>> getAll(){
        return ApiResponse.<List<PermissionReponse>>builder()
                .result(permissionService.getAll())
                .build();
    }
    @DeleteMapping("/{permissionid}")
    ApiResponse<Void> delete(@PathVariable String permissionid){
        permissionService.delete(permissionid);
        return ApiResponse.<Void>builder().build();
    }
}
