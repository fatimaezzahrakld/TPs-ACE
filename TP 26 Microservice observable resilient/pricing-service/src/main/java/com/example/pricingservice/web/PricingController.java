package com.example.pricingservice.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * REST Controller for Pricing operations.
 *
 * <p>
 * This class exposes an endpoint to retrieve the price of a book.
 * It includes logic to simulate forced and random failures for testing
 * resilience mechanisms like circuit breakers and retries.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/prices")
public class PricingController {

    private static final Logger logger = LoggerFactory.getLogger(PricingController.class);

    /**
     * Retrieves the price for a specific book.
     *
     * <p>
     * This endpoint simulates instability:
     * 1. Forced failure if 'fail' parameter is true.
     * 2. Random failure with a 30% probability.
     * </p>
     *
     * @param bookId Identifier of the book
     * @param fail   Whether to force a failure (default: false)
     * @return The calculated price of the book
     * @throws IllegalStateException If a forced or random failure occurs
     */
    @GetMapping("/{bookId}")
    public double price(@PathVariable long bookId,
            @RequestParam(name = "fail", defaultValue = "false") boolean fail) {

        logger.info("REST request to fetch price for book ID: {} (forced fail: {})", bookId, fail);

        // 1) Forced failure for testing purposes
        if (fail) {
            logger.error("Forced failure triggered for book ID: {}", bookId);
            throw new IllegalStateException("Pricing service is down (forced failure)");
        }

        // 2) Random failure (30% probability) to simulate instability
        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            logger.warn("Random failure occurred for book ID: {}", bookId);
            throw new IllegalStateException("Random failure occurred in pricing service");
        }

        // 3) Stable price calculation (example logic)
        double calculatedPrice = 50.0 + (bookId % 10) * 5.0;
        logger.info("Returning price for book ID: {}: {}", bookId, calculatedPrice);
        return calculatedPrice;
    }
}
