package com.example.client.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing a Client in the system.
 *
 * <p>
 * This class defines the data structure for a client, including its
 * unique identifier, name, and age. It is mapped to a database table
 * via JPA/Hibernate.
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
public class Client {

    /**
     * Unique identifier for the client.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the client.
     */
    private String name;

    /**
     * Age of the client in years.
     */
    private Float age;
}
