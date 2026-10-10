package com.foodordering.food_ordering_system.config;

import com.foodordering.food_ordering_system.entity.Role;
import com.foodordering.food_ordering_system.entity.User;
import com.foodordering.food_ordering_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminDataInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail("admin@food.com")) {

                User admin = new User();

                admin.setName("System Admin");
                admin.setEmail("admin@food.com");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("=================================");
                System.out.println("ADMIN ACCOUNT CREATED");
                System.out.println("Email: admin@food.com");
                System.out.println("Password: admin123");
                System.out.println("=================================");
            }
        };
    }
}