package com.hotel.common.dto;

import com.hotel.common.entity.Chambre.TypeChambre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Chambre entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChambreDTO {

    private Long id;

    @NotBlank(message = "Le numéro de chambre est obligatoire")
    private String numero;

    @NotNull(message = "Le type de chambre est obligatoire")
    private TypeChambre type;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prix;

    private Boolean disponible;
    private String description;
    private Integer capaciteMax;
    private List<String> equipements;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
