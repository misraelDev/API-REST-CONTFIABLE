package com.ionic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.ionic")
public class ApiRestfulIonicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestfulIonicApplication.class, args);
	}

}
