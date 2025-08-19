package com.planprostructure.planpro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Value("${spring.datasource.url:NOT_SET}")
    private String databaseUrl;
    
    @Value("${spring.datasource.username:NOT_SET}")
    private String databaseUsername;
    
    @Value("${spring.profiles.active:NOT_SET}")
    private String activeProfile;
    
    @Bean
    public CommandLineRunner databaseConnectionLogger() {
        return args -> {
            logger.info("=== Database Configuration Debug ===");
            logger.info("Active Profile: {}", activeProfile);
            logger.info("Database URL: {}", databaseUrl);
            logger.info("Database Username: {}", databaseUsername);
            logger.info("Database Password: {}", databaseUsername.equals("NOT_SET") ? "NOT_SET" : "***HIDDEN***");
            logger.info("=====================================");
        };
    }
} 