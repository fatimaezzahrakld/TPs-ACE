package com.hotel.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a hotel reservation.
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Le client est obligatoire")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chambre_id", nullable = false)
    @NotNull(message = "La chambre est obligatoire")
    private Chambre chambre;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @Column(length = 2000)
    private String preferences;

    @Column(name = "nombre_personnes")
    private Integer nombrePersonnes;

    @Column(name = "prix_total", precision = 10, scale = 2)
    private BigDecimal prixTotal;

    @Column(length = 500)
    private String commentaires;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculerPrixTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculerPrixTotal();
    }

    /**
     * Calculate total price based on room price and duration.
     */
    private void calculerPrixTotal() {
        if (chambre != null && chambre.getPrix() != null && dateDebut != null && dateFin != null) {
            long nights = java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin);
            prixTotal = chambre.getPrix().multiply(BigDecimal.valueOf(nights));
        }
    }

    /**
     * Enum for reservation status.
     */
    public enum StatutReservation {
        EN_ATTENTE,
        CONFIRMEE,
        ANNULEE,
        TERMINEE
    }
}
