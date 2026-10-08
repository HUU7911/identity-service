package com.huumod.identity.controller;

import com.huumod.identity.dto.ApiResponse;
import com.huumod.identity.dto.request.RoleRequest;
import com.huumod.identity.dto.response.RoleResponse;
import com.huumod.identity.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping("/create")
    ApiResponse<RoleResponse> createPermission(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .results(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .results(roleService.findAllRoles())
                .build();
    }

    @DeleteMapping("/delete/{permission}")
    ApiResponse<Void> deletePermission(@PathVariable String permission) {
        roleService.deleteRoleById(permission);

        return ApiResponse.<Void>builder()
                .message("Role deleted")
                .build();
    }
}
