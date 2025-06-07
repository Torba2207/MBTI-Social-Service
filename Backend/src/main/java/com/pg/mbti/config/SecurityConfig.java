package com.pg.mbti.config;

import com.pg.mbti.config.property.SecurityProperties;
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
                        .requestMatchers(SecurityProperties.Endpoints.PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .securityContext(context -> context
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .sessionManagement(session -> {
                    session.maximumSessions(SecurityProperties.Security.MAX_SESSIONS).maxSessionsPreventsLogin(true);
                    session.sessionFixation().newSession();
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                })
                .logout(logout -> logout
                        .logoutUrl(SecurityProperties.Endpoints.LOGOUT)
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(SecurityProperties.Security.COOKIE_DIRECTIVE)
                        ))
                        .deleteCookies(SecurityProperties.Security.SESSION_COOKIE_NAME)
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                )
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(SecurityProperties.Cors.ALLOWED_ORIGIN_PATTERNS);
        configuration.setAllowedMethods(SecurityProperties.Cors.ALLOWED_METHODS);
        configuration.setAllowedHeaders(SecurityProperties.Cors.ALLOWED_HEADERS);
        configuration.setAllowCredentials(SecurityProperties.Cors.ALLOW_CREDENTIALS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}