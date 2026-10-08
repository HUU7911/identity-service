package com.huumod.identity.service;

import com.huumod.identity.dto.request.RoleRequest;
import com.huumod.identity.dto.response.RoleResponse;
import com.huumod.identity.mapper.RoleMapper;
import com.huumod.identity.repository.PermissionRepository;
import com.huumod.identity.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper  roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse createRole(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deleteRoleById(String role) {
        roleRepository.deleteById(role);
    }
}
