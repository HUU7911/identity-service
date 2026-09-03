package com.ecommere.identity_service.controller;

import com.ecommere.identity_service.dto.ApiResponse;
import com.ecommere.identity_service.dto.request.AuthenticationRequest;
import com.ecommere.identity_service.dto.request.IntrospectRequest;
import com.ecommere.identity_service.dto.request.LogoutRequest;
import com.ecommere.identity_service.dto.request.RefreshTokenRequest;
import com.ecommere.identity_service.dto.response.AuthenticationResponse;
import com.ecommere.identity_service.dto.response.IntrospectResponse;
import com.ecommere.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .results(authenticationService.authenticate(authenticationRequest))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .results(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .message("Successfully logged out")
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var results = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .results(results)
                .build();
    }
}
