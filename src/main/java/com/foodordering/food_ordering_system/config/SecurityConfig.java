package com.foodordering.food_ordering_system.config;

import com.foodordering.food_ordering_system.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    // ================= PASSWORD ENCODER =================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    // ================= SECURITY CONFIGURATION =================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // CSRF disabled for our form-based project
            .csrf(csrf -> csrf.disable())

            .userDetailsService(userDetailsService)


            // ================= AUTHORIZATION =================

            .authorizeHttpRequests(auth -> auth

                // Public pages
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/h2-console/**"
                ).permitAll()


                // Admin
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")


                // Restaurant
                .requestMatchers("/restaurant/**")
                .hasAnyRole("RESTAURANT", "ADMIN")


                // Customer
                .requestMatchers("/customer/**")
                .hasAnyRole("CUSTOMER", "ADMIN")


                // Everything else requires login
                .anyRequest()
                .authenticated()
            )


            // ================= LOGIN =================

            .formLogin(form -> form

                .loginPage("/login")

                .loginProcessingUrl("/login")

                .usernameParameter("email")

                .passwordParameter("password")

                /*
                 * After successful login Spring Security
                 * will use the user's role to decide
                 * where to send them.
                 */
                .successHandler((request, response, authentication) -> {

                    if (authentication.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"))) {

                        response.sendRedirect("/admin/dashboard");

                    } else if (authentication.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                a.getAuthority().equals("ROLE_RESTAURANT"))) {

                        response.sendRedirect("/restaurant/dashboard");

                    } else {

                        response.sendRedirect("/customer/dashboard");

                    }

                })

                .permitAll()
            )


            // ================= LOGOUT =================
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );


        return http.build();
    }
}