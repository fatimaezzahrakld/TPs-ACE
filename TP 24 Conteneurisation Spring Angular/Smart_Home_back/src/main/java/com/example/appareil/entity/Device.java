package com.example.appareil.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA Entity representing a Device (Appareil) in the Smart Home system.
 *
 * <p>
 * This class defines a smart device with its label, description,
 * current state (on/off), and an optional photo. It belongs to a Category.
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
public class Device {

    /**
     * Unique identifier for the device.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Label or name of the device (e.g., Living Room Lamp).
     */
    private String label;

    /**
     * Detailed description of the device's function or location.
     */
    private String description;

    /**
     * Current state of the device (true for ON, false for OFF).
     */
    private boolean state;

    /**
     * Base64 encoded photo or image path of the device.
     * Stored as LONGTEXT to accommodate large image strings.
     */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String photo;

    /**
     * The category this device belongs to.
     */
    @ManyToOne
    private Category category;
}
