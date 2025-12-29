package com.hotel.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a hotel room (Chambre).
 */
@Entity
@Table(name = "chambres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le numéro de chambre est obligatoire")
    @Column(name = "numero", nullable = false, unique = true)
    private String numero;

    @NotBlank(message = "Le type de chambre est obligatoire")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeChambre type;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    @Builder.Default
    private Boolean disponible = true;

    @Column(length = 1000)
    private String description;

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @ElementCollection
    @CollectionTable(name = "chambre_equipements", joinColumns = @JoinColumn(name = "chambre_id"))
    @Column(name = "equipement")
    @Builder.Default
    private List<String> equipements = new ArrayList<>();

    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enum for room types.
     */
    public enum TypeChambre {
        SIMPLE,
        DOUBLE,
        SUITE,
        DELUXE,
        FAMILIALE
    }
}
