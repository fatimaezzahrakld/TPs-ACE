package com.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the Client Service.
 *
 * <p>
 * This microservice is responsible for managing client information.
 * It registers itself with the Eureka discovery server to be accessible
 * by other services in the architecture.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClientApplication.class);

    /**
     * Main entry point for the Client Service application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceClientApplication.class, args);
        logger.info("Client Service started successfully and registered with discovery server.");
    }
}
