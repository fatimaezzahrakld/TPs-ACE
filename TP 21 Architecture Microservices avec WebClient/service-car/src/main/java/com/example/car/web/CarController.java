package com.example.car.web;

import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import com.example.car.services.ClientApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for car management.
 *
 * <p>
 * This class exposes REST endpoints for car operations. It uses
 * the CarRepository for data access and the ClientApi for fetching
 * owner information from the Client Service.
 * </p>
 *
 * <p>
 * Exposed Endpoints:
 * </p>
 * <ul>
 * <li>GET /api/cars: Retrieves all cars with owner details</li>
 * <li>GET /api/cars/byClient/{clientId}: Retrieves all cars for a specific
 * client</li>
 * <li>POST /api/cars: Adds a new car</li>
 * </ul>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarRepository repo;
    private final ClientApi clientApi;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo      The car repository
     * @param clientApi The client API service
     */
    public CarController(CarRepository repo, ClientApi clientApi) {
        this.repo = repo;
        this.clientApi = clientApi;
    }

    /**
     * Adds a new car to the database.
     *
     * @param car The car to save
     * @return ResponseEntity containing the saved car
     */
    @PostMapping
    public ResponseEntity<Car> save(@RequestBody Car car) {
        logger.info("REST request to save new car: {} {}", car.getBrand(), car.getModel());
        Car savedCar = repo.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    /**
     * Retrieves all cars with their owner details.
     *
     * @return List of all cars with populated client information
     */
    @GetMapping
    public List<Car> findAll() {
        logger.info("REST request to fetch all cars with owner details.");
        List<Car> cars = repo.findAll();

        // Enrich each car with client details
        cars.forEach(car -> {
            if (car.getClientId() != null) {
                car.setClient(clientApi.findClientById(car.getClientId()));
            }
        });
        return cars;
    }

    /**
     * Retrieves all cars belonging to a specific client.
     *
     * @param clientId Identifier of the client
     * @return List of cars owned by the specified client
     */
    @GetMapping("/byClient/{clientId}")
    public List<Car> findByClient(@PathVariable Long clientId) {
        logger.info("REST request to fetch cars for client ID: {}", clientId);
        List<Car> cars = repo.findByClientId(clientId);

        // Fetch client details once and apply to all cars
        if (!cars.isEmpty()) {
            com.example.car.entities.Client client = clientApi.findClientById(clientId);
            cars.forEach(car -> car.setClient(client));
        }
        return cars;
    }
}
