package com.example.car.web;

import com.example.car.entities.Client;
import com.example.car.services.ClientApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for testing inter-service communication.
 *
 * <p>
 * This controller provides endpoints to verify that the WebClient
 * communication with the Client Service is working correctly.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final ClientApi clientApi;

    /**
     * Constructor-based dependency injection.
     *
     * @param clientApi The client API service
     */
    public TestController(ClientApi clientApi) {
        this.clientApi = clientApi;
    }

    /**
     * Tests client retrieval by ID.
     *
     * @param id Identifier of the client to test
     * @return The retrieved client or null
     */
    @GetMapping("/client/{id}")
    public Client testClient(@PathVariable Long id) {
        logger.info("Testing client retrieval for ID: {}", id);
        return clientApi.findClientById(id);
    }
}
