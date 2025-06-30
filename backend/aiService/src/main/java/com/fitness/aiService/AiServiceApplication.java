package com.fitness.aiService;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@EnableRabbit
public class AiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiServiceApplication.class, args);
	}

}
