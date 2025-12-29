package com.example.service_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for the API Gateway Service.
 *
 * <p>
 * This class represents the entry point for the API Gateway, which acts
 * as a single point of entry for all microservice clients. The gateway
 * routes requests to appropriate services and can apply cross-cutting
 * policies such as authentication, authorization, logging, etc.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class ServiceGatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(ServiceGatewayApplication.class);

	/**
	 * Main entry point for the Gateway application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServiceGatewayApplication.class, args);
		logger.info("API Gateway started successfully and is ready to route requests.");
	}

	/**
	 * Configures the dynamic route locator based on discovered services.
	 *
	 * <p>
	 * This bean enables dynamic routing by automatically creating route
	 * definitions for services registered in the discovery server (Eureka).
	 * </p>
	 *
	 * @param reactiveDiscoveryClient    The reactive discovery client for service
	 *                                   lookup
	 * @param discoveryLocatorProperties Properties for the discovery locator
	 * @return A configured DiscoveryClientRouteDefinitionLocator
	 */
	@Bean
	public DiscoveryClientRouteDefinitionLocator dynamicRoutes(
			ReactiveDiscoveryClient reactiveDiscoveryClient,
			DiscoveryLocatorProperties discoveryLocatorProperties) {
		logger.info("Initializing dynamic route locator for discovered services.");
		return new DiscoveryClientRouteDefinitionLocator(
				reactiveDiscoveryClient,
				discoveryLocatorProperties);
	}
}
