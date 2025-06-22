package com.pg.mbti.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    // ===== Utility Beans =====

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Provides BCrypt hashing for passwords
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        // JSON serialization/deserialization support
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        // HTTP client for external API calls
        return new RestTemplate();
    }

    // ===== Security Context Beans =====

    @Bean
    public SecurityContextRepository securityContextRepository() {
        // Stores security context in HTTP session
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityContextHolderStrategy securityContextHolderStrategy() {
        // Strategy for accessing the security context
        return SecurityContextHolder.getContextHolderStrategy();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Manages authentication processes
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    // ===== Session Management =====

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        // Publishes session creation/destruction events
        return new HttpSessionEventPublisher();
    }
}