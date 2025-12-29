package com.fatimaezzahrakld.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for the API Routing Gateway.
 *
 * <p>
 * This Spring Cloud Gateway routes incoming requests to the appropriate
 * microservices using service discovery. It integrates with the Eureka server
 * for dynamic routing without manual route configuration.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class RoutingGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(RoutingGatewayApplication.class);

    /**
     * Main entry point for the Gateway application.
     *
     * @param launchArguments Command line arguments
     */
    public static void main(String[] launchArguments) {
        SpringApplication.run(RoutingGatewayApplication.class, launchArguments);
        logger.info("Routing Gateway started successfully.");
    }

    /**
     * Configures dynamic route discovery via Eureka.
     *
     * <p>
     * Enables automatic route creation for services registered in the
     * service registry.
     * </p>
     *
     * @param reactiveClient    The reactive service discovery client
     * @param locatorProperties Configuration properties for the discovery locator
     * @return The configured locator for dynamic routing
     */
    @Bean
    DiscoveryClientRouteDefinitionLocator configureRouteDiscovery(
            ReactiveDiscoveryClient reactiveClient,
            DiscoveryLocatorProperties locatorProperties) {
        logger.info("Configuring dynamic route discovery via Eureka.");
        return new DiscoveryClientRouteDefinitionLocator(reactiveClient, locatorProperties);
    }
}
