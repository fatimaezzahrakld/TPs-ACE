package com.hotel.grpc.mapper;

import com.hotel.common.dto.ChambreDTO;
import com.hotel.common.dto.ClientDTO;
import com.hotel.common.dto.ReservationDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting between DTOs and gRPC proto messages.
 * This is a simplified implementation. In production, you would use
 * the actual generated proto classes.
 */
@Component
public class GrpcMapper {

    public Object toReservationProto(ReservationDTO dto) {
        if (dto == null) return null;
        
        Map<String, Object> proto = new HashMap<>();
        proto.put("id", dto.getId());
        proto.put("clientId", dto.getClientId());
        proto.put("chambreId", dto.getChambreId());
        proto.put("dateDebut", dto.getDateDebut() != null ? dto.getDateDebut().toString() : null);
        proto.put("dateFin", dto.getDateFin() != null ? dto.getDateFin().toString() : null);
        proto.put("statut", dto.getStatut() != null ? dto.getStatut().name() : null);
        proto.put("preferences", dto.getPreferences());
        proto.put("nombrePersonnes", dto.getNombrePersonnes());
        proto.put("prixTotal", dto.getPrixTotal() != null ? dto.getPrixTotal().doubleValue() : null);
        proto.put("commentaires", dto.getCommentaires());
        
        if (dto.getClient() != null) {
            proto.put("client", toClientProto(dto.getClient()));
        }
        
        if (dto.getChambre() != null) {
            proto.put("chambre", toChambreProto(dto.getChambre()));
        }
        
        return proto;
    }

    public Object toReservationListProto(List<ReservationDTO> dtos) {
        Map<String, Object> proto = new HashMap<>();
        proto.put("reservations", dtos.stream()
                .map(this::toReservationProto)
                .toList());
        return proto;
    }

    public Object toClientProto(ClientDTO dto) {
        if (dto == null) return null;
        
        Map<String, Object> proto = new HashMap<>();
        proto.put("id", dto.getId());
        proto.put("nom", dto.getNom());
        proto.put("prenom", dto.getPrenom());
        proto.put("email", dto.getEmail());
        proto.put("telephone", dto.getTelephone());
        proto.put("createdAt", dto.getCreatedAt() != null ? dto.getCreatedAt().toString() : null);
        proto.put("updatedAt", dto.getUpdatedAt() != null ? dto.getUpdatedAt().toString() : null);
        
        return proto;
    }

    public Object toClientListProto(List<ClientDTO> dtos) {
        Map<String, Object> proto = new HashMap<>();
        proto.put("clients", dtos.stream()
                .map(this::toClientProto)
                .toList());
        return proto;
    }

    public Object toChambreProto(ChambreDTO dto) {
        if (dto == null) return null;
        
        Map<String, Object> proto = new HashMap<>();
        proto.put("id", dto.getId());
        proto.put("numero", dto.getNumero());
        proto.put("type", dto.getType() != null ? dto.getType().name() : null);
        proto.put("prix", dto.getPrix() != null ? dto.getPrix().doubleValue() : null);
        proto.put("disponible", dto.getDisponible());
        proto.put("description", dto.getDescription());
        proto.put("capaciteMax", dto.getCapaciteMax());
        proto.put("equipements", dto.getEquipements());
        proto.put("createdAt", dto.getCreatedAt() != null ? dto.getCreatedAt().toString() : null);
        proto.put("updatedAt", dto.getUpdatedAt() != null ? dto.getUpdatedAt().toString() : null);
        
        return proto;
    }

    public Object toChambreListProto(List<ChambreDTO> dtos) {
        Map<String, Object> proto = new HashMap<>();
        proto.put("chambres", dtos.stream()
                .map(this::toChambreProto)
                .toList());
        return proto;
    }

    public Object toDeleteResponseProto(boolean success, String message) {
        Map<String, Object> proto = new HashMap<>();
        proto.put("success", success);
        proto.put("message", message);
        return proto;
    }
}
