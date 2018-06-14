package com.fxi.opn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("file:./application.properties")
@SpringBootApplication
public class OpnApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpnApplication.class, args);
	}
}
