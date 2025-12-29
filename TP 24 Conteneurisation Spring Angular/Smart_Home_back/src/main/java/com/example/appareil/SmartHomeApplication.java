package com.example.appareil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Smart Home Backend Service.
 *
 * <p>
 * This microservice manages smart devices and their categories.
 * It provides a REST API for controlling devices and managing the
 * smart home infrastructure.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class SmartHomeApplication {

    private static final Logger logger = LoggerFactory.getLogger(SmartHomeApplication.class);

    /**
     * Main entry point for the Smart Home application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SmartHomeApplication.class, args);
        logger.info("Smart Home Backend Service started successfully.");
    }

}
