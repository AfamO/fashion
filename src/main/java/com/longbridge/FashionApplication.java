package com.longbridge;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

//@SpringBootApplication
//@EnableConfigurationProperties
//public class FashionApplication extends SpringBootServletInitializer {
//
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(FashionApplication.class);
//	}
//
//
//	public static void main(String[] args) {
//		SpringApplication.run(FashionApplication.class, args);
//	}
//
//	@Bean()
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//	}
//
//
//}

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class FashionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionApplication.class, args);
	}

	@Bean()
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
