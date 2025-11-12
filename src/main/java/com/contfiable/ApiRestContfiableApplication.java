package com.contfiable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.contfiable")
public class ApiRestContfiableApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestContfiableApplication.class, args);
	}

}
