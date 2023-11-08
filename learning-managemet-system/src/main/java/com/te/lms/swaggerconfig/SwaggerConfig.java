package com.te.lms.swaggerconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI baseOpenApi() {
		return new OpenAPI().info(new Info().title("LMS").version("1.0.0").description("Learning Management System"));
	}
}