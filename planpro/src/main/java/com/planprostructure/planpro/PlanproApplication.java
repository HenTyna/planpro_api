package com.planprostructure.planpro;

import com.planprostructure.planpro.properties.JwtProperties;
import com.planprostructure.planpro.utils.PasswordUtils;
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
        System.err.println("encrypted password: " + PasswordUtils.encrypt("1234568"));
    }
}
