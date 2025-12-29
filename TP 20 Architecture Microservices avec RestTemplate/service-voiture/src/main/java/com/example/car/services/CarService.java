package com.example.car.services;

import com.example.car.entities.Car;
import com.example.car.models.CarResponse;
import com.example.car.models.Client;
import com.example.car.repositories.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing Car operations.
 *
 * <p>
 * This class provides business logic for car management, including
 * retrieving all cars and finding a car by ID. It uses RestTemplate to
 * fetch client details from the Client microservice.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Service
public class CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * URL of the client service via the Gateway.
     * In a production environment, this would be configured in application
     * properties.
     */
    private static final String CLIENT_SERVICE_URL = "http://localhost:8888/SERVICE-CLIENT/api/client/";

    /**
     * Retrieves all cars with full client details.
     *
     * @return List of all cars with populated client information
     */
    public List<CarResponse> findAll() {
        logger.info("Fetching all cars from the database.");
        List<Car> cars = carRepository.findAll();

        return cars.stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a car by its identifier with full client details.
     *
     * @param id Identifier of the car
     * @return The corresponding car response
     * @throws Exception If no car is found with the provided ID
     */
    public CarResponse findById(Long id) throws Exception {
        logger.info("Fetching car with ID: {}", id);
        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Car not found with ID: {}", id);
                    return new Exception("Car not found with ID: " + id);
                });

        return mapToCarResponse(car);
    }

    /**
     * Converts a Car entity to a CarResponse by fetching client details.
     *
     * @param car The car entity to map
     * @return A populated CarResponse object
     */
    private CarResponse mapToCarResponse(Car car) {
        logger.debug("Mapping car ID {} to response and fetching client ID {}.", car.getId(), car.getClientId());

        Client client = null;
        try {
            client = restTemplate.getForObject(
                    CLIENT_SERVICE_URL + car.getClientId(),
                    Client.class);
            if (client != null) {
                logger.debug("Successfully fetched client: {}", client.getName());
            }
        } catch (Exception e) {
            logger.error("Error retrieving client details for client ID {}: {}", car.getClientId(), e.getMessage());
        }

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricule(car.getMatricule())
                .client(client)
                .build();
    }
}
