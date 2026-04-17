package com.hms.ProfileMS.config;

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

        RequestMatcher doctorAddWithSecretMatcher = request ->
                "/profile/doctor/add".equals(request.getRequestURI())
                        && "POST".equalsIgnoreCase(request.getMethod())
                        && "SECRET".equals(request.getHeader("X-Secret-Key"));

        RequestMatcher patientAddWithSecretMatcher = request ->
                "/profile/patient/add".equals(request.getRequestURI())
                        && "POST".equalsIgnoreCase(request.getMethod())
                        && "SECRET".equals(request.getHeader("X-Secret-Key"));

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(doctorAddWithSecretMatcher).permitAll()
                        .requestMatchers(patientAddWithSecretMatcher).permitAll()

                        .requestMatchers("/profile/patient/get/**").permitAll()
                        .requestMatchers("/profile/patient/update").permitAll()
                        .requestMatchers("/profile/patient/exists/**").permitAll()

                        .requestMatchers("/profile/doctor/get/**").permitAll()
                        .requestMatchers("/profile/doctor/update").permitAll()
                        .requestMatchers("/profile/doctor/exists/**").permitAll()
                        .requestMatchers("/profile/doctor/dropdowns").permitAll()

                        .anyRequest().denyAll()
                );

        return http.build();
    }
}