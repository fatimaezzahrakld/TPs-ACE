package com.example.bookservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client for interacting with the external Pricing Service.
 *
 * <p>
 * This component uses RestTemplate to fetch book prices. It incorporates
 * Resilience4j patterns (Retry and Circuit Breaker) to handle potential
 * failures or latency in the external service.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Component
public class PricingClient {

    private static final Logger logger = LoggerFactory.getLogger(PricingClient.class);

    private final RestTemplate rest;
    private final String baseUrl;

    /**
     * Constructor for PricingClient with required dependencies.
     *
     * @param rest    The RestTemplate for HTTP requests
     * @param baseUrl The base URL of the pricing service (from properties)
     */
    public PricingClient(RestTemplate rest,
            @Value("${pricing.base-url}") String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    /**
     * Retrieves the price of a book from the pricing service.
     *
     * <p>
     * This method is protected by a Retry mechanism and a Circuit Breaker.
     * If the service is unavailable or fails, a fallback price is returned.
     * </p>
     *
     * @param bookId Identifier of the book
     * @return The price of the book, or 0.0 if not found or on failure
     */
    @Retry(name = "pricing")
    @CircuitBreaker(name = "pricing", fallbackMethod = "fallbackPrice")
    public double getPrice(long bookId) {
        String url = baseUrl + "/api/prices/" + bookId;
        logger.info("Requesting price for book ID: {} from URL: {}", bookId, url);
        Double price = rest.getForObject(url, Double.class);
        return price == null ? 0.0 : price;
    }

    /**
     * Fallback method for getPrice in case of failure or circuit breaker open.
     *
     * @param bookId Identifier of the book
     * @param ex     The exception that triggered the fallback
     * @return A default price of 0.0
     */
    public double fallbackPrice(long bookId, Throwable ex) {
        logger.warn("Fallback triggered for book ID: {}. Reason: {}", bookId, ex.getMessage());
        return 0.0;
    }
}
