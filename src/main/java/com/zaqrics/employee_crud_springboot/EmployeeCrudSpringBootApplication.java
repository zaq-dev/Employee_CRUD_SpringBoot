package com.zaqrics.employee_crud_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmployeeCrudSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeCrudSpringBootApplication.class, args);
	}

}
