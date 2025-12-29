package com.example.bookservice.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Global exception handler for the Book Service API.
 *
 * <p>
 * This class catches specific exceptions thrown by the service layer
 * and maps them to appropriate HTTP status codes and error messages.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestControllerAdvice
public class ApiErrors {

    private static final Logger logger = LoggerFactory.getLogger(ApiErrors.class);

    /**
     * Handles IllegalArgumentException (e.g., book not found, duplicate title).
     *
     * @param ex The caught exception
     * @return A map containing the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(IllegalArgumentException ex) {
        logger.error("Bad request error: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    /**
     * Handles IllegalStateException (e.g., out of stock).
     *
     * @param ex The caught exception
     * @return A map containing the error message
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflict(IllegalStateException ex) {
        logger.error("Conflict error: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }
}
