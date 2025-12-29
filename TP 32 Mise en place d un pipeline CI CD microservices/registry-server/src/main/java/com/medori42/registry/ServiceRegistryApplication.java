package com.fatimaezzahrakld.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for the Service Registry server.
 *
 * <p>
 * Implements a Eureka server for service registration and discovery
 * in a distributed microservices architecture. This server allows other
 * services to register dynamically and discover available instances for
 * communication.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistryApplication.class);

    /**
     * Main entry point for the Service Registry server.
     *
     * @param startupArguments Command line arguments
     */
    public static void main(String[] startupArguments) {
        SpringApplication.run(ServiceRegistryApplication.class, startupArguments);
        logger.info("Service Registry (Eureka Server) started successfully.");
    }
}
