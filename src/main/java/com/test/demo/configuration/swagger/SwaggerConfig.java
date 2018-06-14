package com.test.demo.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Test项目 RESTful APIs")
                        .description("Test项目后台api接口文档")
                        .version("1.1.0.RELEASE")
                        .contact(new Contact("MarryFeng", "http://www.baidu.com", ""))
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.test.demo.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
