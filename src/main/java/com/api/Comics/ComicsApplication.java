package com.api.Comics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComicsApplication {

	public static void main(String[] args) {
	        System.out.println("JDBC_URL: " + System.getenv("JDBC_URL"));
		System.out.println("JDBC_USER_NAME: " + System.getenv("JDBC_USER_NAME"));
		System.out.println("JDBC_PASSWORD: " + System.getenv("JDBC_PASSWORD"));

		SpringApplication.run(ComicsApplication.class, args);
	}

}
