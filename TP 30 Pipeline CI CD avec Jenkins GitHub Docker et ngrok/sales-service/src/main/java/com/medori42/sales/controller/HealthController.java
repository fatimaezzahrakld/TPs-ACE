package com.fatimaezzahrakld.sales.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing health check and information endpoints.
 *
 * <p>
 * This controller exposes basic service information and status endpoints
 * used for monitoring and verifying the deployment in CI/CD pipelines.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    /**
     * Returns a welcome message from the service.
     *
     * @return Welcome message string
     */
    @GetMapping
    public String getWelcomeMessage() {
        logger.info("Welcome endpoint accessed.");
        return "Hello from Spring Boot application :)";
    }

    /**
     * Retrieves user information endpoint.
     *
     * @return User information string
     */
    @GetMapping("/user")
    public String fetchUserList() {
        logger.info("User list endpoint accessed.");
        return "Users List";
    }

    /**
     * Retrieves service presentation details.
     *
     * @return Presentation information string
     */
    @GetMapping("/presentation")
    public String fetchServiceDetails() {
        logger.info("Presentation endpoint accessed.");
        return "Service Presentation Details";
    }
}
