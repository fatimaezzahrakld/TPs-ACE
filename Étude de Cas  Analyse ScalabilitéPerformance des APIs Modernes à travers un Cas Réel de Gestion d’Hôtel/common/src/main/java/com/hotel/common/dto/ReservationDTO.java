package com.hotel.common.dto;

import com.hotel.common.entity.Reservation.StatutReservation;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Reservation entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;

    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;
    
    private ClientDTO client;

    @NotNull(message = "L'ID de la chambre est obligatoire")
    private Long chambreId;
    
    private ChambreDTO chambre;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    private StatutReservation statut;
    private String preferences;
    private Integer nombrePersonnes;
    private BigDecimal prixTotal;
    private String commentaires;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
