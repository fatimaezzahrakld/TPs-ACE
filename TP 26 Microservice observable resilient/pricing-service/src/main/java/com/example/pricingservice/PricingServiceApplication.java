package com.example.pricingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Pricing Service.
 *
 * <p>
 * This microservice provides book pricing information. It is designed
 * to simulate instability (random failures) to test resilience patterns
 * in consuming services.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class PricingServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(PricingServiceApplication.class);

	/**
	 * Main entry point for the Pricing Service application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PricingServiceApplication.class, args);
		logger.info("Pricing Service started successfully.");
	}

}
