package com.cryptoportfolio.cryptoportfoliob.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer getCorsConfiguration() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:3000","https://192.168.1.11:3000","https://pdcryptoportfolio.herokuapp.com/")
					.allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")
					.allowedHeaders("*")
					.allowCredentials(true).exposedHeaders("*");
			}
		};
	}
	
}
