package com.pg.mbti.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
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
