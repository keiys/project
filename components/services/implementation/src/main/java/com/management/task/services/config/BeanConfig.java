package com.management.task.services.config;

import com.management.task.services.security.MD5Encoder;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder getpasswordEncoder() {
        return new MD5Encoder();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createApiSecuritySchema()));
    }

    private SecurityScheme createApiSecuritySchema() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
