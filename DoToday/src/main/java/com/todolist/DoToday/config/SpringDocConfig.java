package com.todolist.DoToday.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("1.0.0")
                .title("TodoDoitAPI");
        return new OpenAPI().info(info);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .displayName("TodoDoit")
                .group("members")
                .pathsToMatch("/api/**")
                .build();
    }
}
