package com.ecommere.identity_service.service;

import com.ecommere.identity_service.constant.RoleDefine;
import com.ecommere.identity_service.dto.request.UserCreationRequest;
import com.ecommere.identity_service.dto.response.UserResponse;
import com.ecommere.identity_service.entity.Role;
import com.ecommere.identity_service.entity.User;
import com.ecommere.identity_service.exception.AppException;
import com.ecommere.identity_service.exception.ErrorCode;
import com.ecommere.identity_service.mapper.UserMapper;
import com.ecommere.identity_service.repository.RoleRepository;
import com.ecommere.identity_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Transactional
    public UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_NOT_EXISTS);

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(RoleDefine.USER.name()).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user =  userRepository.save(user);
        }catch (AppException e){
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(String id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + " not found")
        );

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfor() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS)
        ));
    }
}
