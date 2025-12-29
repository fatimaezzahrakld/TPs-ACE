package com.example.car.services;

import com.example.car.entities.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service for communicating with the Client microservice using WebClient.
 *
 * <p>
 * This class provides methods to retrieve client information from the
 * remote Client Service. It uses a load-balanced WebClient.Builder.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Service
public class ClientApi {

    private static final Logger logger = LoggerFactory.getLogger(ClientApi.class);
    private final WebClient.Builder builder;

    /**
     * Constructor-based dependency injection.
     *
     * @param builder The load-balanced WebClient builder
     */
    public ClientApi(WebClient.Builder builder) {
        this.builder = builder;
    }

    /**
     * Retrieves a client by its identifier from the Client Service.
     *
     * @param id Identifier of the client
     * @return The corresponding client or null if not found/error
     */
    public Client findClientById(Long id) {
        logger.info("Fetching client with ID: {} from remote service.", id);
        try {
            return builder.build()
                    .get()
                    .uri("http://SERVICE-CLIENT/api/clients/" + id)
                    .retrieve()
                    .bodyToMono(Client.class)
                    .onErrorResume(e -> {
                        logger.error("Error fetching client with ID {}: {}", id, e.getMessage());
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception e) {
            logger.error("Unexpected error during WebClient call for client ID {}: {}", id, e.getMessage());
            return null;
        }
    }
}
