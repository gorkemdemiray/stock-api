package com.stockapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration for Stock API
 * 
 * @author gorkemdemiray
 * 
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * 
	 * @return {@link Docket} for Stock API
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.stockapi.controller"))
				.paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	/**
	 * 
	 * @return {@link ApiInfo} to construct configuration
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Stock API")
				.description("RESTful Web Service for stock application")
				.contact(new Contact("Gorkem Demiray", "https://github.com/gorkemdemiray", "gorkemdemiray@gmail.com"))
				.version("1.0.0").build();
	}
}
