package com.example.card_app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "MBank by Max Sirotkin",
				description = "Api of bank", version = "1.0.0",
				contact = @Contact(
						name = "Sirotkin Max",
						email = "galunas2015@gmail.com"
				)
		)
)

@SpringBootApplication
public class CardAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardAppApplication.class, args);
	}

}
