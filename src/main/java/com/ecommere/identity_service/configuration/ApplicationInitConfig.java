package com.ecommere.identity_service.configuration;

import com.ecommere.identity_service.constant.RoleDefine;
import com.ecommere.identity_service.entity.Role;
import com.ecommere.identity_service.entity.User;
import com.ecommere.identity_service.repository.RoleRepository;
import com.ecommere.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    final String ADMIN_USERNAME = "admin";

    @NonFinal
    final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner initApplicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                roleRepository.save(Role.builder()
                        .name(RoleDefine.USER.name())
                        .description("has role user")
                        .build()
                );

                Role adminRole = roleRepository.save(
                        Role.builder()
                                .name(RoleDefine.ADMIN.name())
                                .description("has role admin")
                                .build()
                );

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .firstName("tran")
                        .lastName("huu")
                        .email("trihuutran4@gmail.com")
                        .birthDate(LocalDate.of(2006, 4, 5))
                        .build();
                userRepository.save(user);
                log.info("admin user has created with admin password {}", ADMIN_PASSWORD);
            }
            log.info("Application has been initialized...");
        };
    }
}
