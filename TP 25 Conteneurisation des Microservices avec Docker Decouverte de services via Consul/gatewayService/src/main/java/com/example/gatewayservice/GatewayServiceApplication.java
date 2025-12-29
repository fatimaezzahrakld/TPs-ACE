package com.example.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the Gateway Service.
 *
 * <p>
 * This service acts as an API Gateway, routing requests to the appropriate
 * microservices. It uses Consul for service discovery to dynamically
 * resolve service instances.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApplication.class);

	/**
	 * Main entry point for the Gateway Service application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
		logger.info("Gateway Service started successfully and is ready to route requests.");
	}
}
