package com.hotel.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for Client entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
