package com.example.bookservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration class for HTTP clients.
 *
 * <p>
 * This class defines beans for HTTP communication, specifically
 * configuring the RestTemplate with appropriate timeouts.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Configuration
public class HttpConfig {

    private static final Logger logger = LoggerFactory.getLogger(HttpConfig.class);

    /**
     * Configures a RestTemplate bean with connection and read timeouts.
     *
     * @param builder The RestTemplateBuilder provided by Spring Boot
     * @return A configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        logger.info("Initializing RestTemplate with 1s connect timeout and 2s read timeout.");
        return builder
                .setConnectTimeout(Duration.ofSeconds(1))
                .setReadTimeout(Duration.ofSeconds(2))
                .build();
    }
}
