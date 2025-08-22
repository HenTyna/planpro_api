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
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Value("${spring.profiles.active:NOT_SET}")
    private String activeProfile;
    
    /**
     * Custom DataSource configuration for production/railway environments
     * This will only be used when the 'production' or 'railway' profile is active
     * For local development, Spring Boot's auto-configuration will handle H2
     */
    @Bean
    @Primary
    @Profile({"production", "railway"})
    public DataSource dataSource() {
        // Manually resolve environment variables
        String databaseUrl = resolveDatabaseUrl();
        String username = resolveUsername();
        String password = resolvePassword();
        
        logger.info("=== Creating Production DataSource ===");
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
        // Try multiple environment variable names for Railway
        String[] possibleUrls = {
            System.getenv("DATABASE_URL"),
            System.getenv("Postgres.DATABASE_URL"),
            System.getenv("POSTGRES_DATABASE_URL"),
            System.getenv("JDBC_DATABASE_URL"),
            // Railway specific variables
            System.getenv("PROD_DB_HOST") != null ? 
                String.format("jdbc:postgresql://%s:%s/%s", 
                    System.getenv("PROD_DB_HOST"),
                    System.getenv("PROD_DB_PORT"),
                    System.getenv("PROD_DB_NAME")) : null
        };
        
        for (String url : possibleUrls) {
            if (url != null && !url.trim().isEmpty()) {
                return url;
            }
        }
        
        // Fallback for local PostgreSQL development
        return "jdbc:postgresql://localhost:5432/planpro";
    }
    
    private String resolveUsername() {
        // Try multiple environment variable names
        String[] possibleUsernames = {
            System.getenv("DB_USERNAME"),
            System.getenv("Postgres.POSTGRES_USER"),
            System.getenv("POSTGRES_USER"),
            System.getenv("PGUSER"),
            System.getenv("PROD_DB_USERNAME")
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
            System.getenv("PGPASSWORD"),
            System.getenv("PROD_DB_PASSWORD")
        };
        
        for (String password : possiblePasswords) {
            if (password != null && !password.trim().isEmpty()) {
                return password;
            }
        }
        
        return "postgres";
    }
    
    @Bean
    @Profile({"production", "railway"})
    public CommandLineRunner databaseConnectionLogger(DataSource dataSource) {
        return args -> {
            logger.info("=== Production Database Configuration Debug ===");
            logger.info("Active Profile: {}", activeProfile);
            
            // Show all relevant environment variables
            logger.info("=== Environment Variables ===");
            String[] relevantVars = {
                "DATABASE_URL", "DB_USERNAME", "DB_PASSWORD",
                "Postgres.DATABASE_URL", "Postgres.POSTGRES_USER", "Postgres.POSTGRES_PASSWORD",
                "PGHOST", "PGPORT", "PGUSER", "PGPASSWORD",
                "POSTGRES_USER", "POSTGRES_PASSWORD", "POSTGRES_DATABASE_URL",
                "PROD_DB_HOST", "PROD_DB_PORT", "PROD_DB_NAME", "PROD_DB_USERNAME", "PROD_DB_PASSWORD",
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
                logger.info("✅ Production database connection successful!");
                logger.info("Database Product: {}", connection.getMetaData().getDatabaseProductName());
                logger.info("Database Version: {}", connection.getMetaData().getDatabaseProductVersion());
            } catch (SQLException e) {
                logger.error("❌ Production database connection failed: {}", e.getMessage());
                logger.error("SQL State: {}", e.getSQLState());
                logger.error("Error Code: {}", e.getErrorCode());
            }
            
            logger.info("=====================================");
        };
    }
    
    /**
     * Local development database connection logger
     * This will only run for local profile to show H2 connection info
     */
    @Bean
    @Profile("local")
    public CommandLineRunner localDatabaseConnectionLogger(DataSource dataSource) {
        return args -> {
            logger.info("=== Local Development Database Configuration ===");
            logger.info("Active Profile: {}", activeProfile);
            logger.info("Using H2 in-memory database for local development");
            
            try (Connection connection = dataSource.getConnection()) {
                logger.info("✅ H2 database connection successful!");
                logger.info("Database Product: {}", connection.getMetaData().getDatabaseProductName());
                logger.info("Database Version: {}", connection.getMetaData().getDatabaseProductVersion());
                logger.info("H2 Console available at: http://localhost:8080/h2-console");
                logger.info("JDBC URL: jdbc:h2:mem:planpro");
                logger.info("Username: sa");
                logger.info("Password: (empty)");
            } catch (SQLException e) {
                logger.error("❌ H2 database connection failed: {}", e.getMessage());
            }
            
            logger.info("=====================================");
        };
    }
} 