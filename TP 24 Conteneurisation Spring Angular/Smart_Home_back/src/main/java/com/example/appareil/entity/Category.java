package com.example.appareil.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * JPA Entity representing a Category in the Smart Home system.
 *
 * <p>
 * This class defines a category that can group multiple devices.
 * It is mapped to a database table via JPA/Hibernate.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Unique identifier for the category.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Label or name of the category (e.g., Lighting, Security).
     */
    private String label;

    /**
     * List of devices belonging to this category.
     * Uses eager fetching to load devices with the category.
     */
    @OneToMany(targetEntity = Device.class, mappedBy = "category", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "category" })
    private List<Device> deviceList;
}
