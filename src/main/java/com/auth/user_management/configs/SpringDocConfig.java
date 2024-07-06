package com.auth.user_management.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI().info(new Info().title("User Management API").description("Documentation for User " +
                "Management API").version("v0.0.1")).addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(
                        new Components()
                                .addSecuritySchemes("Bearer Token",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }
}
