package com.example.airline_booking_system.config;

import com.example.airline_booking_system.user.Role;
import com.example.airline_booking_system.user.User;
import com.example.airline_booking_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    @Value("${app.admin-email}")
    private String adminEmail;
    @Value("${app.admin-password}")
    private String adminPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {

        if(userRepository.findByEmail(adminEmail).isPresent()){
            return;
        }
        User admin = User.builder()
                .role(Role.ADMIN)
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .enabled(true)
                .build();
        userRepository.save(admin);
    }
}
