package com.ecommere.identity_service.service;

import com.ecommere.identity_service.dto.request.RoleRequest;
import com.ecommere.identity_service.dto.response.RoleResponse;
import com.ecommere.identity_service.entity.Role;
import com.ecommere.identity_service.mapper.RoleMapper;
import com.ecommere.identity_service.repository.PermissionRepository;
import com.ecommere.identity_service.repository.RoleRepository;
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
