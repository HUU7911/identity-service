package com.huumod.identity.mapper;

import com.huumod.identity.dto.request.PermissionRequest;
import com.huumod.identity.dto.response.PermissionResponse;
import com.huumod.identity.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
