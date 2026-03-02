package com.mglopez.crudstore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "API CRUD STORE",
                description = "Aplicacion con productos y categorias que simula un inventario",
                termsOfService = "www.github.com/MiguelG0929",
                version = "1.0.0",
                contact = @Contact(
                        name = "Miguel López",
                        url = "www.github.com/MiguelG0929",
                        email = "lopezirahetamiguelgerardo@gmail.com"
                ),
                license = @License(
                        name = "Copyright (c) 2026 MiguelLópez",
                        url = "www.github.com/MiguelG0929"
                )
        ),

        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:9525"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = "http://crudstoreapp:8080"
                )
        },

        security = @SecurityRequirement(
                name = "Security Token"
        )


)


@SecurityScheme(
        name = "Security Token",
        description = "Access Token para mi API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)


public class SwaggerConfig {
}
