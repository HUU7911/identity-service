package com.ecommere.identity_service.controller;

import com.ecommere.identity_service.dto.ApiResponse;
import com.ecommere.identity_service.dto.request.PermissionRequest;
import com.ecommere.identity_service.dto.response.PermissionResponse;
import com.ecommere.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping("/create")
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .results(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .results(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/delete/{permission}")
    ApiResponse<PermissionResponse> deletePermission(@PathVariable String permission) {
        permissionService.delete(permission);

        return ApiResponse.<PermissionResponse>builder()
                .message("Permission deleted")
                .build();
    }
}
