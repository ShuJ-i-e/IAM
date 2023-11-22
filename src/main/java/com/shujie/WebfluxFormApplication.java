package com.shujie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@EnableR2dbcRepositories
@SpringBootApplication
public class WebfluxFormApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxFormApplication.class, args);
	}

}
