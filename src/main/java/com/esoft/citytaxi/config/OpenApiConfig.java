package com.esoft.citytaxi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("City Taxi Backend API")
                        .version("1.0")
                        .description("This backend service for the Taxi Application handles key features such as driver and passenger management, fare calculation, and ride tracking.")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@citytaxi.com")
                                .url("https://wb.com")));
    }
}
