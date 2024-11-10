package rs.ac.uns.ftn.onlybunsapp.config;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        ArrayList<Server> servers = new ArrayList<>(3);
        servers.add(new Server().url("http://localhost:8082/").description("development server"));
        servers.add(new Server().url("http://qa:8081/").description("test server"));
        servers.add(new Server().url("http://www.rest-example.com/").description("production server"));

        // U ovom delu se koristi novi način za dodavanje sigurnosnog elementa
        return new OpenAPI()
                .info(new Info()
                        .title("Rest example")
                        .description("Rest example with OpenAPI documentation")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("FTN")
                                .url("https://github.com/stojkovm")
                                .email("ftn@uns.ac.rs"))
                        .termsOfService("TOC")
                        .license(new License().name("License").url("#"))
                )
                .servers(servers)
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"));
    }
}
