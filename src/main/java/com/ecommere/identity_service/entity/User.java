package com.ecommere.identity_service.entity;

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

    String username;
    String password;

    @Column(name = "firstname")
    String firstName;

    @Column(name = "lastname")
    String lastName;

    String email;

    LocalDate birthDate;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
