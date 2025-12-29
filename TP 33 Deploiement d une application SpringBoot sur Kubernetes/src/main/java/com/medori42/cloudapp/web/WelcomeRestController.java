package com.fatimaezzahrakld.cloudapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller providing welcome endpoints for the cloud application.
 *
 * <p>
 * Demonstrates Kubernetes ConfigMap integration with Spring Boot by exposing
 * an endpoint that returns a configurable welcome message.
 * </p>
 *
 * <p>
 * Key features:
 * <ul>
 * <li>RESTful endpoint for welcome messages</li>
 * <li>Externalized configuration via environment variables</li>
 * <li>Kubernetes ConfigMap integration</li>
 * </ul>
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see org.springframework.web.bind.annotation.RestController
 */
@RestController
public class WelcomeRestController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeRestController.class);

    /**
     * Key used for the message in the JSON response.
     */
    private static final String RESPONSE_MESSAGE_KEY = "message";

    /**
     * Key used for the status in the JSON response.
     */
    private static final String RESPONSE_STATUS_KEY = "status";

    /**
     * Status value indicating a successful operation.
     */
    private static final String STATUS_SUCCESS_VALUE = "OK";

    /**
     * Welcome message configured via Kubernetes ConfigMap or application
     * properties.
     *
     * <p>
     * Injected from the {@code APP_MESSAGE} environment variable, with a fallback
     * default value if not configured.
     * </p>
     */
    @Value("${APP_MESSAGE:Welcome from the default value}")
    private String welcomeMessageContent;

    /**
     * Default constructor for the welcome controller.
     */
    public WelcomeRestController() {
        // Default constructor
    }

    /**
     * Retrieves a welcome message with the application status.
     *
     * <p>
     * Demonstrates how Kubernetes ConfigMaps can be used to externalize
     * configuration in containerized Spring Boot applications. The message
     * can be updated by modifying the ConfigMap without rebuilding the app.
     * </p>
     *
     * @return A Map containing the welcome message and application status
     */
    @GetMapping("/api/hello")
    public Map<String, String> retrieveWelcomeMessage() {
        logger.info("REST request to retrieve welcome message.");
        return buildResponsePayload();
    }

    /**
     * Builds the response payload containing the message and status.
     *
     * @return The response Map with message and status
     */
    private Map<String, String> buildResponsePayload() {
        Map<String, String> responsePayload = new HashMap<>();
        responsePayload.put(RESPONSE_MESSAGE_KEY, getWelcomeMessageContent());
        responsePayload.put(RESPONSE_STATUS_KEY, STATUS_SUCCESS_VALUE);
        return responsePayload;
    }

    /**
     * Retrieves the configured welcome message content.
     *
     * @return The configured welcome message
     */
    private String getWelcomeMessageContent() {
        return this.welcomeMessageContent;
    }
}
