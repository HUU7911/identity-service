package com.ecommere.identity_service.mapper;

import com.ecommere.identity_service.dto.request.PermissionRequest;
import com.ecommere.identity_service.dto.response.PermissionResponse;
import com.ecommere.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
