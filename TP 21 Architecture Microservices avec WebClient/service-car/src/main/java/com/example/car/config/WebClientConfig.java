package com.example.car.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient.
 *
 * <p>
 * This class defines beans for WebClient instances used in inter-service
 * communication. It includes load-balancing capabilities for resolving
 * service names via Eureka.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Configuration
public class WebClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    /**
     * Configures a load-balanced WebClient.Builder.
     *
     * <p>
     * The @LoadBalanced annotation enables the WebClient to resolve
     * service names (e.g., SERVICE-CLIENT) using the discovery server.
     * </p>
     *
     * @return A configured WebClient.Builder
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        logger.info("Initializing load-balanced WebClient.Builder bean.");
        return WebClient.builder();
    }
}
