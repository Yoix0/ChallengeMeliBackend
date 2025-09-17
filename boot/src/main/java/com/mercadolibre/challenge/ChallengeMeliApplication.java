package com.mercadolibre.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mercadolibre.challenge")
@SpringBootConfiguration
public class ChallengeMeliApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChallengeMeliApplication.class, args);
    }
}