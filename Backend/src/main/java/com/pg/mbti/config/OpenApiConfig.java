package com.pg.mbti.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI documentation.
 * Provides Swagger UI and API documentation endpoints for the application.
 */
@Configuration
public class OpenApiConfig {

    // ===== API Documentation =====

    @Bean
    public OpenAPI customOpenAPI() {
        // Configures the OpenAPI documentation with application information
        // including title, version, description, and support contact details
        return new OpenAPI()
                .info(new Info()
                        .title("API documentation")
                        .version("2.0")
                        .description("API documentation for MBTI social network service")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("mbtiservice2@gmail.com")));
    }
}