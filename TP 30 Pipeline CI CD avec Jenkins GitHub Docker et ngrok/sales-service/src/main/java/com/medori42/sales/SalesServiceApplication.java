package com.fatimaezzahrakld.sales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Main application class for the Sales Service.
 *
 * <p>
 * This Spring Boot application provides REST endpoints for sales operations.
 * It is configured to exclude data source auto-configuration as it primarily
 * serves as a demonstration for CI/CD pipelines.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SalesServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(SalesServiceApplication.class);

    /**
     * Main entry point for the Sales Service application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SalesServiceApplication.class, args);
        logger.info("Sales Service Application started successfully.");
    }
}
