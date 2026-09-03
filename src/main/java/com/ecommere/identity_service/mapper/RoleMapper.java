package com.ecommere.identity_service.mapper;

import com.ecommere.identity_service.dto.request.RoleRequest;
import com.ecommere.identity_service.dto.response.RoleResponse;
import com.ecommere.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    @Mapping(target = "name")
    RoleResponse toRoleResponse(Role role);
}
