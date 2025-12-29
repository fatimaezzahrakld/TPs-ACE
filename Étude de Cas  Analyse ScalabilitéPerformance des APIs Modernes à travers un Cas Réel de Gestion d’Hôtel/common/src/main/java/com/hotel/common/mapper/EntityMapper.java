package com.hotel.common.mapper;

import com.hotel.common.dto.ClientDTO;
import com.hotel.common.dto.ChambreDTO;
import com.hotel.common.dto.ReservationDTO;
import com.hotel.common.entity.Client;
import com.hotel.common.entity.Chambre;
import com.hotel.common.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for entity-DTO conversions.
 */
@Component
public class EntityMapper {

    // ==================== CLIENT MAPPING ====================
    
    public ClientDTO toClientDTO(Client entity) {
        if (entity == null) return null;
        return ClientDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Client toClient(ClientDTO dto) {
        if (dto == null) return null;
        return Client.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .build();
    }

    public List<ClientDTO> toClientDTOList(List<Client> entities) {
        return entities.stream()
                .map(this::toClientDTO)
                .collect(Collectors.toList());
    }

    // ==================== CHAMBRE MAPPING ====================
    
    public ChambreDTO toChambreDTO(Chambre entity) {
        if (entity == null) return null;
        return ChambreDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .type(entity.getType())
                .prix(entity.getPrix())
                .disponible(entity.getDisponible())
                .description(entity.getDescription())
                .capaciteMax(entity.getCapaciteMax())
                .equipements(entity.getEquipements())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Chambre toChambre(ChambreDTO dto) {
        if (dto == null) return null;
        return Chambre.builder()
                .id(dto.getId())
                .numero(dto.getNumero())
                .type(dto.getType())
                .prix(dto.getPrix())
                .disponible(dto.getDisponible() != null ? dto.getDisponible() : true)
                .description(dto.getDescription())
                .capaciteMax(dto.getCapaciteMax())
                .equipements(dto.getEquipements())
                .build();
    }

    public List<ChambreDTO> toChambreDTOList(List<Chambre> entities) {
        return entities.stream()
                .map(this::toChambreDTO)
                .collect(Collectors.toList());
    }

    // ==================== RESERVATION MAPPING ====================
    
    public ReservationDTO toReservationDTO(Reservation entity) {
        if (entity == null) return null;
        return ReservationDTO.builder()
                .id(entity.getId())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .client(toClientDTO(entity.getClient()))
                .chambreId(entity.getChambre() != null ? entity.getChambre().getId() : null)
                .chambre(toChambreDTO(entity.getChambre()))
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .statut(entity.getStatut())
                .preferences(entity.getPreferences())
                .nombrePersonnes(entity.getNombrePersonnes())
                .prixTotal(entity.getPrixTotal())
                .commentaires(entity.getCommentaires())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Reservation toReservation(ReservationDTO dto, Client client, Chambre chambre) {
        if (dto == null) return null;
        return Reservation.builder()
                .id(dto.getId())
                .client(client)
                .chambre(chambre)
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .statut(dto.getStatut())
                .preferences(dto.getPreferences())
                .nombrePersonnes(dto.getNombrePersonnes())
                .commentaires(dto.getCommentaires())
                .build();
    }

    public List<ReservationDTO> toReservationDTOList(List<Reservation> entities) {
        return entities.stream()
                .map(this::toReservationDTO)
                .collect(Collectors.toList());
    }
}
