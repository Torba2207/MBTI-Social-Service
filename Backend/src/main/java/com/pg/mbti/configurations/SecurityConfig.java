package com.pg.mbti.configurations;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http
    ) throws Exception {
        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
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
                .securityContext((context) -> context.securityContextRepository(securityContextRepository))
                .sessionManagement((session) -> {
                            session.maximumSessions(1).maxSessionsPreventsLogin(true);
                            session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
                            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                        }
                )
                .logout((logout) -> {
                            logout.logoutUrl("/api/auth/logout");
                            logout.addLogoutHandler(
                                    new HeaderWriterLogoutHandler(
                                            new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)
                                    )
                            );
                            logout.deleteCookies("MY_SESSION_COOKIE");
                            logout.logoutSuccessHandler(customLogoutSuccessHandler);
                        }
                )
                .authenticationProvider(authenticationProvider)
                .build();
    }
}