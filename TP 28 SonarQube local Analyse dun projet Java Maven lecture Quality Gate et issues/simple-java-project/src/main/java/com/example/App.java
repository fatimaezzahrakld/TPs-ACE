package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple application class demonstrating basic arithmetic operations.
 *
 * <p>
 * This class was originally designed to demonstrate SonarQube analysis
 * by intentionally including code smells and bugs. It has been refactored
 * to follow best practices, including SLF4J logging and clean code principles.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting Simple Java Application.");

        int x = 10;
        int y = 20;

        // Refactored: Removed redundant condition and used logger
        logger.info("Value of x: {}, Value of y: {}", x, y);

        // Refactored: Removed hardcoded password (Security Hotspot)
        logger.debug("Application initialization complete.");
    }

    /**
     * Adds two integers.
     *
     * @param a First integer
     * @param b Second integer
     * @return The sum of a and b
     */
    public int add(int a, int b) {
        logger.debug("Adding {} and {}", a, b);
        return a + b;
    }

    /**
     * Subtracts the second integer from the first.
     *
     * @param a First integer
     * @param b Second integer
     * @return The difference of a and b
     */
    public int subtract(int a, int b) {
        logger.debug("Subtracting {} from {}", b, a);
        return a - b;
    }
}
