package com.fatimaezzahrakld.clientmanagement.web;

import com.fatimaezzahrakld.clientmanagement.domain.ClientEntity;
import com.fatimaezzahrakld.clientmanagement.application.ClientBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing endpoints for client management.
 *
 * <p>
 * Provides a RESTful API for CRUD operations on clients.
 * Endpoints are accessible via the {@code /api/client} prefix.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see ClientEntity
 * @see ClientBusinessService
 */
@RestController
@RequestMapping("api/client")
public class ClientRestController {

    private static final Logger logger = LoggerFactory.getLogger(ClientRestController.class);

    private final ClientBusinessService clientBusinessService;

    /**
     * Constructor with business service injection.
     *
     * @param clientBusinessService The client management service
     */
    @Autowired
    public ClientRestController(ClientBusinessService clientBusinessService) {
        this.clientBusinessService = clientBusinessService;
    }

    /**
     * Retrieves the complete list of clients.
     *
     * @return A list of all client entities
     */
    @GetMapping
    public List<ClientEntity> getAllClients() {
        logger.info("REST request to fetch all clients.");
        return clientBusinessService.retrieveAllClients();
    }

    /**
     * Retrieves a specific client by its identifier.
     *
     * @param clientIdentifier The unique identifier of the client
     * @return The corresponding client entity
     */
    @GetMapping("/{id}")
    public ClientEntity getClientById(@PathVariable("id") Long clientIdentifier) {
        logger.info("REST request to fetch client with ID: {}", clientIdentifier);
        return clientBusinessService.retrieveClientById(clientIdentifier);
    }

    /**
     * Creates a new client in the system.
     *
     * @param clientData The data of the client to create
     * @return A HTTP response with the creation status
     */
    @PostMapping
    public ResponseEntity<Void> createNewClient(@RequestBody ClientEntity clientData) {
        logger.info("REST request to create new client: {}", clientData.getCompleteName());
        clientBusinessService.registerNewClient(clientData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
