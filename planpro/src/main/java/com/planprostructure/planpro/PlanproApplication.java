package com.planprostructure.planpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ConfigurationPropertiesScan("com.planprostructure.planpro.properties")
@EnableRetry
public class PlanproApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlanproApplication.class, args);
    }
}
