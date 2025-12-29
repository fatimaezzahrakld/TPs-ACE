package com.example.car.controllers;

import com.example.car.models.CarResponse;
import com.example.car.services.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for car management.
 *
 * <p>
 * This class exposes REST endpoints for car operations. It uses
 * the CarService for business logic and data aggregation from multiple
 * services.
 * </p>
 *
 * <p>
 * Exposed Endpoints:
 * </p>
 * <ul>
 * <li>GET /api/car: Retrieves all cars with client details</li>
 * <li>GET /api/car/{id}: Retrieves a car by its ID with client details</li>
 * </ul>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("api/car")
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    /**
     * Retrieves all cars with their owner details.
     *
     * @return List of all cars
     */
    @GetMapping
    public List<CarResponse> findAll() {
        logger.info("REST request to fetch all cars with owner details.");
        return carService.findAll();
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id Identifier of the car
     * @return ResponseEntity containing the car response or an error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        logger.info("REST request to fetch car with ID: {}", id);
        try {
            CarResponse car = carService.findById(id);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            logger.error("Error fetching car with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}
