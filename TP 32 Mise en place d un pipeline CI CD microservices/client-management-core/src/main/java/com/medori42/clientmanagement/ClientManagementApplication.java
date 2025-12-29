package com.fatimaezzahrakld.clientmanagement;

import com.fatimaezzahrakld.clientmanagement.domain.ClientEntity;
import com.fatimaezzahrakld.clientmanagement.persistence.ClientDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for the Client Management microservice.
 *
 * <p>
 * This Spring Boot application provides a REST API for managing client
 * information in a distributed microservices environment. It registers itself
 * with the Eureka server for service discovery and dynamic routing.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see ClientEntity
 * @see ClientDataRepository
 */
@SpringBootApplication
public class ClientManagementApplication {

    private static final Logger logger = LoggerFactory.getLogger(ClientManagementApplication.class);

    /**
     * Creates and configures the bean for initial demonstration data.
     *
     * <p>
     * This bean executes at application startup to populate the database
     * with test records.
     * </p>
     *
     * @param clientDataRepository The repository for client data access
     * @return A CommandLineRunner that initializes demonstration data
     */
    @Bean
    CommandLineRunner loadInitialData(ClientDataRepository clientDataRepository) {
        return commandLineArgs -> {
            logger.info("Initializing demonstration data for clients.");
            clientDataRepository.save(new ClientEntity(1L, "Amine SAFI", 23.0f));
            clientDataRepository.save(new ClientEntity(2L, "Amal ALAOUI", 22.0f));
            clientDataRepository.save(new ClientEntity(3L, "Samir RAMI", 22.0f));
            logger.info("Demonstration data initialization complete.");
        };
    }

    /**
     * Main entry point for the Client Management application.
     *
     * @param applicationArgs Command line arguments
     */
    public static void main(String[] applicationArgs) {
        SpringApplication.run(ClientManagementApplication.class, applicationArgs);
        logger.info("Client Management Application started successfully.");
    }
}
