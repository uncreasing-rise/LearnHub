package com.example.learnhub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${project.openapi.dev-url}")
    private String devUrl;


    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");


        Contact contact = new Contact();
        contact.setEmail("email@example.com");
        contact.setName("Author");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("Management API: PROJECT LEARN HUB")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints to manage project.").termsOfService("https://www.google.com")
            .license(mitLicense);

        SecurityScheme securitySchemeBear = new SecurityScheme()
            .description("Security authorization bearer")
            .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER).name("Authorization");


        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes("Authorization", securitySchemeBear))
            .info(info)
            .addSecurityItem(new SecurityRequirement().addList("Authorization", Arrays.asList("read", "write")))
            .servers(List.of(devServer));
    }
}
