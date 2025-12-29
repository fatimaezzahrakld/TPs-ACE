package com.example.car.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing a Car in the system.
 *
 * <p>
 * This class defines the data structure for a car, including its
 * brand, model, and the ID of its owner client. It also includes a
 * transient Client object for response enrichment.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    /**
     * Unique identifier for the car.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Brand of the car (e.g., Toyota, Renault).
     */
    private String brand;

    /**
     * Model of the car (e.g., Corolla, Megane).
     */
    private String model;

    /**
     * Identifier of the client who owns the car.
     * This ID is used to fetch client details from the Client Service.
     */
    private Long clientId;

    /**
     * Full details of the owner client.
     * Marked as @Transient because it is not persisted in the car database.
     */
    @Transient
    private Client client;
}
