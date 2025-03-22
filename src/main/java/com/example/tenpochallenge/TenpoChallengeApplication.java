package com.example.tenpochallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.example.tenpochallenge")
@EnableCaching
public class TenpoChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenpoChallengeApplication.class, args);
	}

}
