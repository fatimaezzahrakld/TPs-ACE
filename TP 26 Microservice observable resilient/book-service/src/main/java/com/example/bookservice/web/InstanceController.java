package com.example.bookservice.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Debug controller for retrieving instance information.
 *
 * <p>
 * This controller provides an endpoint to identify the current service
 * instance, which is useful for debugging in distributed environments
 * or when using load balancers.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
public class InstanceController {

    private static final Logger logger = LoggerFactory.getLogger(InstanceController.class);

    @Value("${server.port}")
    private int port;

    /**
     * Retrieves information about the current service instance.
     *
     * @return A string containing the hostname and internal port
     */
    @GetMapping("/api/debug/instance")
    public String instance() {
        logger.info("REST request to fetch instance information.");
        String host = System.getenv().getOrDefault("HOSTNAME", "local");
        return "instance=" + host + " internalPort=" + port;
    }
}
