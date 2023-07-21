package com.study.todoappreview.global.swagger;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("투두 비슷한 게시판")
                .version("v0.0.1")
                .description("데모 투두 프로젝트");
        return new OpenAPI().components(new Components()).info(info);
    }
}
