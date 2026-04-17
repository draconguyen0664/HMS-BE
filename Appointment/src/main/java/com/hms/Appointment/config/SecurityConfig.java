package com.hms.Appointment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        RequestMatcher scheduleWithSecretMatcher = request ->
                "/appointment/schedule".equals(request.getRequestURI())
                        && "POST".equalsIgnoreCase(request.getMethod())
                        && "SECRET".equals(request.getHeader("X-Secret-Key"));

        RequestMatcher cancelWithSecretMatcher = request ->
                request.getRequestURI().startsWith("/appointment/cancel/")
                        && "PUT".equalsIgnoreCase(request.getMethod())
                        && "SECRET".equals(request.getHeader("X-Secret-Key"));

        RequestMatcher getWithSecretMatcher = request ->
                request.getRequestURI().startsWith("/appointment/get/")
                        && "GET".equalsIgnoreCase(request.getMethod())
                        && "SECRET".equals(request.getHeader("X-Secret-Key"));

        RequestMatcher getDetailsWithSecretMatcher = request ->
                request.getRequestURI().startsWith("/appointment/get/details/")
                        && "GET".equalsIgnoreCase(request.getMethod())
                        && "SECRET".equals(request.getHeader("X-Secret-Key"));

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(scheduleWithSecretMatcher).permitAll()
                        .requestMatchers(cancelWithSecretMatcher).permitAll()
                        .requestMatchers(getWithSecretMatcher).permitAll()
                        .requestMatchers(getDetailsWithSecretMatcher).permitAll()
                        .anyRequest().denyAll()
                );

        return http.build();
    }
}