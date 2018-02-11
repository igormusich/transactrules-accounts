package com.transactrules.accounts.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(apiInfo())
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "TransactRules Account API",
                "Account servicing APIs by TransactRules.",
                "API TOS",
                "Terms of service",
                new Contact("Igor Music", "www.transactrules.com", "igor@transactrules.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
