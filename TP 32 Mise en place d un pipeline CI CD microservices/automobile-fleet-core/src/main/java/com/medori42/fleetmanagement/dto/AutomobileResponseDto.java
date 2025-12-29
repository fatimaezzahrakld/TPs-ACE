package com.fatimaezzahrakld.fleetmanagement.dto;

import com.fatimaezzahrakld.fleetmanagement.domain.OwnerClientData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for automobile API responses.
 *
 * <p>
 * Combines automobile information with owner data to provide a complete view.
 * Used as the response model for REST endpoints requiring enriched information.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomobileResponseDto {

    /**
     * Unique identifier of the automobile.
     */
    private Long automobileIdentifier;

    /**
     * Name of the manufacturer.
     */
    private String manufacturerName;

    /**
     * Designation of the model.
     */
    private String modelName;

    /**
     * Registration plate number.
     */
    private String registrationPlate;

    /**
     * Information of the associated owner.
     */
    private OwnerClientData ownerInformation;
}
