package com.huumod.identity.mapper;

import com.huumod.identity.dto.request.RoleRequest;
import com.huumod.identity.dto.response.RoleResponse;
import com.huumod.identity.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    @Mapping(target = "name")
    RoleResponse toRoleResponse(Role role);
}
