package com.huumod.identity.controller;

import com.huumod.identity.dto.ApiResponse;
import com.huumod.identity.dto.request.AuthenticationRequest;
import com.huumod.identity.dto.request.IntrospectRequest;
import com.huumod.identity.dto.request.LogoutRequest;
import com.huumod.identity.dto.request.RefreshTokenRequest;
import com.huumod.identity.dto.response.AuthenticationResponse;
import com.huumod.identity.dto.response.IntrospectResponse;
import com.huumod.identity.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/outbound/identity")
    ApiResponse<AuthenticationResponse> outboundIdentity(@RequestParam("code") String code) {
        return ApiResponse.<AuthenticationResponse>builder()
                .results(authenticationService.outboundIdentity(code))
                .build();
    }
}
