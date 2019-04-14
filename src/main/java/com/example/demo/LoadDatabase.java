package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(UserRepository repository) {
		return args -> {
			log.info("Preloading " + repository.save(new User("John", 2500.05f)));
			log.info("Preloading " + repository.save(new User("Mary Posa", 4000f)));
		};
	}
}