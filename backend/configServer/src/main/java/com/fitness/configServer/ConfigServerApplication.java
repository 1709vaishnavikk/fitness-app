package com.fitness.configServer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {



	public static void main(String[] args) {

		SpringApplication.run(ConfigServerApplication.class, args);
//		System.out.println("Config loaded: " + System.getProperty("spring.config.location"));


	}



}
