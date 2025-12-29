package com.fatimaezzahrakld.fleetmanagement.application;

import com.fatimaezzahrakld.fleetmanagement.domain.AutomobileEntity;
import com.fatimaezzahrakld.fleetmanagement.domain.OwnerClientData;
import com.fatimaezzahrakld.fleetmanagement.dto.AutomobileResponseDto;
import com.fatimaezzahrakld.fleetmanagement.persistence.AutomobileDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Business service for managing automobile fleet operations.
 *
 * <p>
 * Orchestrates calls to the persistence layer and communication with the
 * Client Management service. This service enriches automobile data with
 * owner information retrieved via REST.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see AutomobileEntity
 * @see AutomobileDataRepository
 */
@Service
public class FleetBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(FleetBusinessService.class);

    /**
     * Base URL for the Client Management service.
     */
    private static final String CLIENT_SERVICE_BASE_URL = "http://localhost:8888/CLIENT-MANAGEMENT-SERVICE";

    private final AutomobileDataRepository automobileRepository;
    private final RestTemplate httpRestTemplate;

    /**
     * Constructor with dependency injection.
     *
     * @param automobileRepository The repository for automobile data access
     * @param httpRestTemplate     The REST template for inter-service communication
     */
    @Autowired
    public FleetBusinessService(AutomobileDataRepository automobileRepository, RestTemplate httpRestTemplate) {
        this.automobileRepository = automobileRepository;
        this.httpRestTemplate = httpRestTemplate;
    }

    /**
     * Retrieves all automobiles with their owner information.
     *
     * <p>
     * Performs a call to the Client Management service to enrich the data.
     * </p>
     *
     * @return A list of DTOs containing automobiles and their owners
     */
    public List<AutomobileResponseDto> retrieveAllAutomobiles() {
        logger.info("Retrieving all automobiles from the database.");
        List<AutomobileEntity> automobileList = automobileRepository.findAll();

        logger.info("Fetching all client data from Client Management service.");
        ResponseEntity<OwnerClientData[]> clientServiceResponse = httpRestTemplate.getForEntity(
                CLIENT_SERVICE_BASE_URL + "/api/client",
                OwnerClientData[].class);

        OwnerClientData[] ownerDataArray = clientServiceResponse.getBody();

        return automobileList.stream()
                .map(automobile -> convertToResponseDto(automobile, ownerDataArray))
                .toList();
    }

    /**
     * Retrieves a specific automobile by its identifier along with its owner.
     *
     * @param automobileId The unique identifier of the automobile
     * @return The DTO containing complete information
     * @throws IllegalArgumentException If the automobile ID is invalid or not found
     */
    public AutomobileResponseDto retrieveAutomobileById(Long automobileId) {
        logger.info("Retrieving automobile with ID: {}", automobileId);
        AutomobileEntity automobile = automobileRepository.findById(automobileId)
                .orElseThrow(() -> {
                    logger.error("Invalid automobile identifier: {}", automobileId);
                    return new IllegalArgumentException("Invalid automobile identifier: " + automobileId);
                });

        logger.info("Fetching owner data for client ID: {}", automobile.getOwnerClientId());
        OwnerClientData ownerData = httpRestTemplate.getForObject(
                CLIENT_SERVICE_BASE_URL + "/api/client/" + automobile.getOwnerClientId(),
                OwnerClientData.class);

        return buildAutomobileResponse(automobile, ownerData);
    }

    /**
     * Converts an automobile entity to a response DTO by matching the owner.
     *
     * @param automobile     The automobile entity to convert
     * @param ownerDataArray The array of owner data retrieved from the client
     *                       service
     * @return The constructed response DTO
     */
    private AutomobileResponseDto convertToResponseDto(AutomobileEntity automobile, OwnerClientData[] ownerDataArray) {
        OwnerClientData matchingOwner = null;
        if (ownerDataArray != null) {
            matchingOwner = Arrays.stream(ownerDataArray)
                    .filter(owner -> owner.getOwnerIdentifier().equals(automobile.getOwnerClientId()))
                    .findFirst()
                    .orElse(null);
        }

        return buildAutomobileResponse(automobile, matchingOwner);
    }

    /**
     * Assembles a response object from automobile and owner data.
     *
     * @param automobile The source automobile entity
     * @param ownerData  The owner data
     * @return The assembled response DTO
     */
    private AutomobileResponseDto buildAutomobileResponse(AutomobileEntity automobile, OwnerClientData ownerData) {
        return AutomobileResponseDto.builder()
                .automobileIdentifier(automobile.getAutomobileId())
                .manufacturerName(automobile.getManufacturerBrand())
                .ownerInformation(ownerData)
                .registrationPlate(automobile.getLicensePlateNumber())
                .modelName(automobile.getModelDesignation())
                .build();
    }
}
