package com.fatimaezzahrakld.fleetmanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing an automobile in the fleet management system.
 *
 * <p>
 * This class models the essential information of a vehicle and its
 * association with an owner client. Each vehicle is linked to a client
 * via the {@code ownerClientId} which refers to the Client Management service.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Entity
@Table(name = "automobiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomobileEntity {

    /**
     * Unique technical identifier for the automobile.
     * Automatically generated using the identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "automobile_id")
    private Long automobileId;

    /**
     * Brand name of the automobile manufacturer.
     * Examples: Toyota, BMW, Mercedes-Benz.
     */
    @Column(name = "manufacturer_brand", nullable = false)
    private String manufacturerBrand;

    /**
     * Designation of the automobile model.
     * Commercial name of the specific model.
     */
    @Column(name = "model_designation")
    private String modelDesignation;

    /**
     * Official registration number of the vehicle.
     * License plate or registration number.
     */
    @Column(name = "license_plate", unique = true)
    private String licensePlateNumber;

    /**
     * Identifier of the client who owns the vehicle.
     * Reference to the Client Management microservice.
     */
    @Column(name = "owner_client_id")
    private Long ownerClientId;
}
