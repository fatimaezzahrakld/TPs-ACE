package com.fatimaezzahrakld.platformcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Platform Core Application.
 *
 * <p>
 * Demonstrates a modern cloud-native architecture with Spring Boot
 * and Kubernetes integration.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class PlatformCoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(PlatformCoreApplication.class);

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(final String[] args) {
        PlatformCoreApplication app = new PlatformCoreApplication();
        app.start(args);
    }

    /**
     * Starts the Spring Boot application using an instance method.
     *
     * @param startupArgs Arguments for application startup
     */
    private void start(final String[] startupArgs) {
        SpringApplication.run(PlatformCoreApplication.class, startupArgs);
        logger.info("Platform Core Application started successfully.");
    }

    /**
     * Default constructor for PlatformCoreApplication.
     */
    public PlatformCoreApplication() {
        // Default constructor
    }
}
