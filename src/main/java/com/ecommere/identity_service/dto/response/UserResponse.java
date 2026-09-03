package com.ecommere.identity_service.dto.response;

import com.ecommere.identity_service.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;

    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    LocalDate birthDate;
    Set<Role> roles;
}
