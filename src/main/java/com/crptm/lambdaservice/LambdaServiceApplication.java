package com.crptm.lambdaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.crptm.lambdaservice")
public class LambdaServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LambdaServiceApplication.class, args);
	}
}
