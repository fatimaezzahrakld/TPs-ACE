package com.fatimaezzahrakld.fleetmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing owner client information.
 *
 * <p>
 * Used to receive data from the Client Management service via REST API.
 * This class is not a JPA entity as client data is managed by a separate
 * microservice.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerClientData {

    /**
     * Unique identifier of the owner client.
     */
    private Long ownerIdentifier;

    /**
     * Full name of the owner.
     */
    private String ownerFullName;

    /**
     * Age of the owner in years.
     */
    private Integer ownerAge;
}
