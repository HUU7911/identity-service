package com.huumod.identity.entity;

import com.huumod.identity.convert.EncryptConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", nullable = false, unique = true)
    @Convert(converter = EncryptConverter.class)
    String username;

    @Size(min = 6)
    @Column(name = "password", unique = true)
    String password;

    @Column(name = "firstname")
    @Convert(converter = EncryptConverter.class)
    String firstName;

    @Column(name = "lastname")
    @Convert(converter = EncryptConverter.class)
    String lastName;

    @Column(name = "email")
    @Convert(converter = EncryptConverter.class)
    String email;

    @Column(name = "birthdate")
    LocalDate birthDate;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
