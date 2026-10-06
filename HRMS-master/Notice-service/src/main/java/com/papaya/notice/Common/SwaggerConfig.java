package com.papaya.notice.Common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Notice Management System API",
                version = "1.0",
                description = "This is the API documentation for the Notice Management System.",
                contact = @Contact(
                        name = "Anil Gupta",
                        email = "anil000@gmail.com",
                        url = "https://github.com/AnilGupta9118"
                )
        )
)
public class SwaggerConfig {
    // Optional: Swagger config bean, agar aur customize karna ho
}

