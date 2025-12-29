package com.example.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the Car Service.
 *
 * <p>
 * This microservice is responsible for managing car information and
 * communicating with the Client Service using WebClient. It registers
 * itself with the Eureka discovery server.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceCarApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServiceCarApplication.class);

    /**
     * Main entry point for the Car Service application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceCarApplication.class, args);
        logger.info("Car Service started successfully and registered with discovery server.");
    }
}
