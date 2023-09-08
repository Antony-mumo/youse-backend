package com.company.youse.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableSwagger2
public class SpringFoxConfig extends WebMvcConfigurationSupport {

    @Bean
    Docket api()
    {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
//                .securitySchemes(Collections.singletonList(apiKey()))
//                .securityContexts(Collections.singletonList(securityContext()))
                .genericModelSubstitutes(Optional.class);
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Youse Service",
                "Perform youse requests" + "",
                "0.0.1",
                "https://youse.co.ke",
                new Contact("Austin Oyugi", "https://youse.co.ke", "austinoyugi@gmail.com"),
                "info@youse.co.ke",
                "License of API",
                Collections.emptyList());
    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .forPaths(PathSelectors.any())
//                .build();
//    }

//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
//    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
