package com.example.client.web;

import com.example.client.entities.Client;
import com.example.client.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for client management.
 *
 * <p>
 * This class exposes REST endpoints for client operations. It uses
 * the ClientRepository for data access.
 * </p>
 *
 * <p>
 * Exposed Endpoints:
 * </p>
 * <ul>
 * <li>GET /api/clients: Retrieves all clients</li>
 * <li>GET /api/clients/{id}: Retrieves a client by its ID</li>
 * <li>POST /api/clients: Adds a new client</li>
 * </ul>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo The client repository
     */
    public ClientController(ClientRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all clients.
     *
     * @return List of all clients
     */
    @GetMapping
    public List<Client> findAll() {
        logger.info("REST request to fetch all clients.");
        return repo.findAll();
    }

    /**
     * Retrieves a client by its ID.
     *
     * @param id Identifier of the client
     * @return ResponseEntity containing the client or a not found status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        logger.info("REST request to fetch client with ID: {}", id);
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.error("Client not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Adds a new client.
     *
     * @param client The client to add
     * @return The saved client
     */
    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client) {
        logger.info("REST request to save new client: {}", client.getName());
        Client savedClient = repo.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }
}
