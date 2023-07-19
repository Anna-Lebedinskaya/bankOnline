package com.bankproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI libraryProject(){
        return new OpenAPI()
                .info(new Info()
                        .title("Банковская система онлайн")
                        .description("Сервис, позволяющий управлять банковскими счетами онлайн")
                        .version("0.1")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                        .contact(new Contact().name("Anna Lebedinskaya").email("annavolchkova@mail.ru").url(""))
                );
    }
}
