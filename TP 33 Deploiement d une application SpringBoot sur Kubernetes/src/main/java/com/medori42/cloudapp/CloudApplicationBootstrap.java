package com.fatimaezzahrakld.cloudapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main bootstrap class for the Spring Boot Cloud Application.
 *
 * <p>
 * This application demonstrates the deployment of a Spring Boot application
 * on Kubernetes with ConfigMap integration, security configuration, and
 * RESTful API endpoints.
 * </p>
 *
 * <p>
 * The application uses Spring Boot 3.2.0 with Java 17 and provides a
 * production-ready cloud-native architecture.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 */
@SpringBootApplication
public class CloudApplicationBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(CloudApplicationBootstrap.class);

    /**
     * Default constructor for the bootstrap class.
     *
     * <p>
     * Required by Spring Boot for application initialization.
     * </p>
     */
    public CloudApplicationBootstrap() {
        // Default constructor
    }

    /**
     * Main entry point for the Spring Boot application.
     *
     * <p>
     * Initializes and starts the Spring application context, bootstrapping
     * all configured components and services.
     * </p>
     *
     * @param applicationArguments Command line arguments passed to the application
     */
    public static void main(final String[] applicationArguments) {
        initializeApplication(applicationArguments);
    }

    /**
     * Initialization method for the Spring Boot application.
     *
     * <p>
     * Encapsulates the application startup logic and can be extended for
     * additional configurations.
     * </p>
     *
     * @param runtimeArguments The application startup arguments
     */
    private static void initializeApplication(final String[] runtimeArguments) {
        SpringApplication.run(CloudApplicationBootstrap.class, runtimeArguments);
        logger.info("Cloud Application Bootstrap started successfully.");
    }
}
