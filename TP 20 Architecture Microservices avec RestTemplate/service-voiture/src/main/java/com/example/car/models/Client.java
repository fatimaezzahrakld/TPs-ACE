package com.example.car.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing a Client in the Car service.
 *
 * <p>
 * This class is a data transfer object (DTO) used to store client
 * information retrieved from the Client microservice via RestTemplate.
 * It is not persisted in the Car service's database.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    /**
     * Unique identifier for the client.
     */
    private Long id;

    /**
     * Full name of the client.
     */
    private String name;

    /**
     * Age of the client in years.
     */
    private Integer age;
}
