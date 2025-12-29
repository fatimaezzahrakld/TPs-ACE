package com.example.client_service.services;

import com.example.client_service.entities.Client;
import com.example.client_service.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing Client operations.
 *
 * <p>This class provides business logic for client management, including
 * retrieving all clients, finding a client by ID, and adding new clients.</p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Retrieves all clients from the database.
     *
     * @return List of all clients
     */
    public List<Client> findAll() {
        logger.info("Fetching all clients from the database.");
        return clientRepository.findAll();
    }

    /**
     * Retrieves a client by its identifier.
     *
     * @param id Identifier of the client
     * @return The corresponding client
     * @throws Exception If no client is found with the provided ID
     */
    public Client findById(Long id) throws Exception {
        logger.info("Fetching client with ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", id);
                    return new Exception("Client not found with ID: " + id);
                });
    }

    /**
     * Adds a new client or updates an existing one.
     *
     * @param client The client to save
     * @return The saved client with its generated ID
     */
    public Client addClient(Client client) {
        logger.info("Saving client: {}", client.getName());
        return clientRepository.save(client);
    }
}
