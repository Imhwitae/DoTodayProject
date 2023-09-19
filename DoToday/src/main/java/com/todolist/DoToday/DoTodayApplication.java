package com.todolist.DoToday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
public class DoTodayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoTodayApplication.class, args);
	}

}
