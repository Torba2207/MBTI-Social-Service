package com.pg.mbti.configurations;

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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/confirm-email",
                                "/api/auth/forgot-password",
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
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