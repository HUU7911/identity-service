package com.ecommere.identity_service.service;

import com.ecommere.identity_service.dto.request.AuthenticationRequest;
import com.ecommere.identity_service.dto.request.IntrospectRequest;
import com.ecommere.identity_service.dto.request.LogoutRequest;
import com.ecommere.identity_service.dto.request.RefreshTokenRequest;
import com.ecommere.identity_service.dto.response.AuthenticationResponse;
import com.ecommere.identity_service.dto.response.IntrospectResponse;
import com.ecommere.identity_service.entity.InvalidateToken;
import com.ecommere.identity_service.entity.User;
import com.ecommere.identity_service.exception.AppException;
import com.ecommere.identity_service.exception.ErrorCode;
import com.ecommere.identity_service.repository.InvalidateTokenRepository;
import com.ecommere.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {

    final UserRepository userRepository;
    final InvalidateTokenRepository invalidateTokenRepository;

    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    int validateDuration;

    @Value("${jwt.refreshable-duration}")
    int refreshableDuration;

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken();

        boolean valid = true;

        try {
            verifyToken(token, true);
        }catch (AppException | JOSEException | ParseException e) {
            valid = false;
        }

        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS)
        );

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isValid)
            throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isValid)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                .plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!verified && expiryTime.after(new Date())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return signedJWT;
    }

    public void logout(LogoutRequest request) {
        try {
            var sigToken = verifyToken(request.getToken(), true);

            String jti = sigToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = sigToken.getJWTClaimsSet().getExpirationTime();

            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .Id(jti)
                    .expiryTime(expiryTime)
                    .build();
            invalidateTokenRepository.save(invalidateToken);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("Logout failed", e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var sigJWT = verifyToken(request.getToken(), true);

        var jti = sigJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = sigJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken validate = InvalidateToken.builder()
                .Id(jti)
                .expiryTime(expiryTime)
                .build();
        invalidateTokenRepository.save(validate);

        var username = sigJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findById(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        var generatedToken = generateToken(user);

        return AuthenticationResponse.builder()
                .token(generatedToken)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("huumod.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validateDuration, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (Exception e) {
            log.info("can't create token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission ->
                        stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
