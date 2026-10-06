package com.example.Client.Service.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Client Service API",
                version = "API Documentation For Client Service",
                description = "v1.0",
                contact = @Contact(
                        name ="vivek",
                        email = "vivek@gmail.com"
                )

        )
)

public class SwaggerConfig {
}
