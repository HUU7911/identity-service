package com.ecommere.identity_service.entity;

import com.ecommere.identity_service.convert.EncryptConverter;
import jakarta.persistence.*;
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
@Table(name = "Users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id")
    String id;

    @Convert(converter = EncryptConverter.class)
    String username;

    String password;

    @Column(name = "firstname")
    @Convert(converter = EncryptConverter.class)
    String firstName;

    @Column(name = "lastname")
    @Convert(converter = EncryptConverter.class)
    String lastName;

    @Convert(converter = EncryptConverter.class)
    String email;

    LocalDate birthDate;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
