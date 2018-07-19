package com.longbridge;

import com.longbridge.repository.CustomJpaRepositoryFactoryBean;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
public class FashionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionApplication.class, args);
	}

	@Bean()
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
