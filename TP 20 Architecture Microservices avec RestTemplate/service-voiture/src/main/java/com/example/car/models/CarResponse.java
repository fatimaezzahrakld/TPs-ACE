package com.example.car.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for car responses.
 *
 * <p>
 * This class combines car information with full client details to provide
 * a comprehensive response to the API consumers. It uses the Builder pattern
 * for flexible object creation.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {

    /**
     * Unique identifier for the car.
     */
    private Long id;

    /**
     * Brand of the car.
     */
    private String brand;

    /**
     * Model of the car.
     */
    private String model;

    /**
     * Registration number of the car.
     */
    private String matricule;

    /**
     * Full details of the client who owns the car.
     */
    private Client client;
}
