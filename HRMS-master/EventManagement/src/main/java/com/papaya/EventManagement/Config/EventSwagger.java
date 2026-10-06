package com.papaya.EventManagement.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Event Management API",
                version = "API Documentation For Event Management System",
                description = "v1.0",
                contact = @Contact(
                        name ="vivek",
                        email = "vivek@gmail.com"
                )

        )
)
public class EventSwagger{
}
