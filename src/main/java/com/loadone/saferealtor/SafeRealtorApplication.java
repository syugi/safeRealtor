package com.loadone.saferealtor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SafeRealtorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeRealtorApplication.class, args);
	}

}
