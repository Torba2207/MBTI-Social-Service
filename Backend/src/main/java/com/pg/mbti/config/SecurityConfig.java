package com.pg.mbti.config;

import com.pg.mbti.util.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/**
 * Configuration for application security.
 * Manages authentication, authorization, CORS, sessions, and logout handling.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    // ===== Security Filter Chain =====

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configures the main security filter chain with:
        // - CSRF disabled for API endpoints
        // - CORS configuration for cross-origin requests
        // - URL-based authorization rules
        // - Session management with max sessions and fixation protection
        // - Custom logout handling with cookie clearing
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/confirm-email",
                                "/api/auth/forgot-password",
                                "/api/enums/mbti",
                                "/api/answers",
                                "/api/questions",
                                "/v3/api-docs"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .securityContext(context -> context
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .sessionManagement(session -> {
                    session.maximumSessions(1).maxSessionsPreventsLogin(true);
                    session.sessionFixation().newSession();
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                })
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)
                        ))
                        .deleteCookies("MY_SESSION_COOKIE")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                )
                .authenticationProvider(authenticationProvider)
                .build();
    }

    // ===== CORS Configuration =====

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Configures Cross-Origin Resource Sharing (CORS) to allow frontend access
        // from different origins while maintaining security
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}