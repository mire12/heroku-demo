package com.itradix.ehealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.itradix.ehealth.dao" })
public class EhealthApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(EhealthApplication.class, args);
	}

}
