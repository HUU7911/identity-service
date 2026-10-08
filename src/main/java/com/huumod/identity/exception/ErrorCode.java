package com.ecommere.identity_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    INVALID_KEY(1001, "invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1002, "unauthorized", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED(9999, "uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTS(1005, "user not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "unauthenticated", HttpStatus.UNAUTHORIZED),
    ;

    int code;
    String message;
    HttpStatusCode httpStatus;
}
