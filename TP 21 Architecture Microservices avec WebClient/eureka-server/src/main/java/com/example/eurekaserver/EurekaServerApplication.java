package com.example.eurekaserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for the Eureka Service Discovery Server.
 *
 * <p>
 * This class serves as the entry point for the Eureka Server application,
 * which acts as a service discovery server in a microservices architecture.
 * The Eureka server allows services to register themselves and discover
 * other services dynamically.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(EurekaServerApplication.class);

    /**
     * Main entry point for the Eureka Server application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        logger.info("Eureka Server started successfully and is ready for service registration.");
    }
}
