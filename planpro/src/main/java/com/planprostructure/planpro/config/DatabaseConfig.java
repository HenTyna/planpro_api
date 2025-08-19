package com.planprostructure.planpro.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Value("${spring.profiles.active:NOT_SET}")
    private String activeProfile;
    
    @Bean
    @Primary
    public DataSource dataSource() {
        // Manually resolve environment variables
        String databaseUrl = resolveDatabaseUrl();
        String username = resolveUsername();
        String password = resolvePassword();
        
        logger.info("=== Creating DataSource ===");
        logger.info("Database URL: {}", databaseUrl);
        logger.info("Username: {}", username);
        logger.info("Password: {}", password.equals("") ? "EMPTY" : "***HIDDEN***");
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setConnectionTimeout(30000);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1200000);
        
        return new HikariDataSource(config);
    }
    
    private String resolveDatabaseUrl() {
        // Try multiple environment variable names
        String[] possibleUrls = {
            System.getenv("DATABASE_URL"),
            System.getenv("Postgres.DATABASE_URL"),
            System.getenv("POSTGRES_DATABASE_URL"),
            System.getenv("JDBC_DATABASE_URL")
        };
        
        for (String url : possibleUrls) {
            if (url != null && !url.trim().isEmpty()) {
                return url;
            }
        }
        
        // Fallback for local development
        return "jdbc:postgresql://localhost:5432/planpro";
    }
    
    private String resolveUsername() {
        // Try multiple environment variable names
        String[] possibleUsernames = {
            System.getenv("DB_USERNAME"),
            System.getenv("Postgres.POSTGRES_USER"),
            System.getenv("POSTGRES_USER"),
            System.getenv("PGUSER")
        };
        
        for (String username : possibleUsernames) {
            if (username != null && !username.trim().isEmpty()) {
                return username;
            }
        }
        
        return "postgres";
    }
    
    private String resolvePassword() {
        // Try multiple environment variable names
        String[] possiblePasswords = {
            System.getenv("DB_PASSWORD"),
            System.getenv("Postgres.POSTGRES_PASSWORD"),
            System.getenv("POSTGRES_PASSWORD"),
            System.getenv("PGPASSWORD")
        };
        
        for (String password : possiblePasswords) {
            if (password != null && !password.trim().isEmpty()) {
                return password;
            }
        }
        
        return "postgres";
    }
    
    @Bean
    public CommandLineRunner databaseConnectionLogger(DataSource dataSource) {
        return args -> {
            logger.info("=== Database Configuration Debug ===");
            logger.info("Active Profile: {}", activeProfile);
            
            // Show all relevant environment variables
            logger.info("=== Environment Variables ===");
            String[] relevantVars = {
                "DATABASE_URL", "DB_USERNAME", "DB_PASSWORD",
                "Postgres.DATABASE_URL", "Postgres.POSTGRES_USER", "Postgres.POSTGRES_PASSWORD",
                "PGHOST", "PGPORT", "PGUSER", "PGPASSWORD",
                "POSTGRES_USER", "POSTGRES_PASSWORD", "POSTGRES_DATABASE_URL",
                "SPRING_PROFILES_ACTIVE"
            };
            
            for (String var : relevantVars) {
                String value = System.getenv(var);
                if (value != null) {
                    logger.info("{}: {}", var, var.contains("PASSWORD") ? "***HIDDEN***" : value);
                } else {
                    logger.info("{}: NOT_SET", var);
                }
            }
            
            // Test the actual connection
            try (Connection connection = dataSource.getConnection()) {
                logger.info("✅ Database connection successful!");
                logger.info("Database Product: {}", connection.getMetaData().getDatabaseProductName());
                logger.info("Database Version: {}", connection.getMetaData().getDatabaseProductVersion());
            } catch (SQLException e) {
                logger.error("❌ Database connection failed: {}", e.getMessage());
                logger.error("SQL State: {}", e.getSQLState());
                logger.error("Error Code: {}", e.getErrorCode());
            }
            
            logger.info("=====================================");
        };
    }
} 