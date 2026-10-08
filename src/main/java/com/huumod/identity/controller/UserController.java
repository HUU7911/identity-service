package com.ecommere.identity_service.controller;

import com.ecommere.identity_service.dto.ApiResponse;
import com.ecommere.identity_service.dto.request.UserCreationRequest;
import com.ecommere.identity_service.dto.response.UserResponse;
import com.ecommere.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/create")
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .results(userService.createUser(request))
                .build();
    }

    @GetMapping("/{Id}")
    ApiResponse<UserResponse> getUser(@PathVariable String Id) {
        return ApiResponse.<UserResponse>builder()
                .results(userService.getUserById(Id))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .results(userService.getAllUsers())
                .build();
    }

    @DeleteMapping("/delete/{Id}")
    ApiResponse<Void> deleteUser(@PathVariable String Id) {
        userService.deleteUserById(Id);

        return ApiResponse.<Void>builder()
                .message("User deleted successfully")
                .build();
    }

    @GetMapping("/myInfor")
    ApiResponse<UserResponse> getMyInFor() {
        return ApiResponse.<UserResponse>builder()
                .results(userService.getMyInfor())
                .build();
    }
}
