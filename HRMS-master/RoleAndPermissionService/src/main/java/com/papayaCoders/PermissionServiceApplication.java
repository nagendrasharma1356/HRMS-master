package com.papayaCoders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PermissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PermissionServiceApplication.class, args);
	}

}
