package com.example.client_service.controllers;

import com.example.client_service.entities.Client;
import com.example.client_service.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for client management.
 *
 * <p>This class exposes REST endpoints for client operations. It uses
 * the ClientService for business logic and data access.</p>
 *
 * <p>Exposed Endpoints:</p>
 * <ul>
 *   <li>GET /api/client: Retrieves all clients</li>
 *   <li>GET /api/client/{id}: Retrieves a client by its ID</li>
 *   <li>POST /api/client: Adds a new client</li>
 * </ul>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("api/client")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService service;

    /**
     * Retrieves all clients.
     *
     * @return List of all clients
     */
    @GetMapping
    public List<Client> findAll() {
        logger.info("REST request to fetch all clients.");
        return service.findAll();
    }

    /**
     * Retrieves a client by its ID.
     *
     * @param id Identifier of the client
     * @return ResponseEntity containing the client or an error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        logger.info("REST request to fetch client with ID: {}", id);
        try {
            Client client = service.findById(id);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            logger.error("Error fetching client with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Adds a new client.
     *
     * @param client The client to add
     * @return ResponseEntity containing the saved client
     */
    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client) {
        logger.info("REST request to save new client: {}", client.getName());
        Client savedClient = service.addClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }
}
