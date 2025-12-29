package com.fatimaezzahrakld.clientmanagement.application;

import com.fatimaezzahrakld.clientmanagement.domain.ClientEntity;
import com.fatimaezzahrakld.clientmanagement.persistence.ClientDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business service for managing client operations.
 *
 * <p>
 * Implements business logic and orchestrates calls to the persistence layer.
 * This service is injected into REST controllers to handle client-related
 * requests.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see ClientEntity
 * @see ClientDataRepository
 */
@Service
public class ClientBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(ClientBusinessService.class);

    private final ClientDataRepository clientRepository;

    /**
     * Constructor with dependency injection.
     *
     * @param clientRepository The repository for client data access
     */
    @Autowired
    public ClientBusinessService(ClientDataRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Retrieves all registered clients.
     *
     * @return A list containing all clients
     */
    public List<ClientEntity> retrieveAllClients() {
        logger.info("Retrieving all clients from the database.");
        return clientRepository.findAll();
    }

    /**
     * Searches for a specific client by its identifier.
     *
     * @param clientId The unique identifier of the client
     * @return The corresponding client entity
     * @throws IllegalArgumentException If the identifier does not match any client
     */
    public ClientEntity retrieveClientById(Long clientId) {
        logger.info("Retrieving client with ID: {}", clientId);
        return clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Invalid client identifier: {}", clientId);
                    return new IllegalArgumentException("Invalid client identifier: " + clientId);
                });
    }

    /**
     * Registers a new client in the database.
     *
     * @param newClient The client entity to persist
     */
    public void registerNewClient(ClientEntity newClient) {
        logger.info("Registering new client: {}", newClient.getCompleteName());
        clientRepository.save(newClient);
    }
}
