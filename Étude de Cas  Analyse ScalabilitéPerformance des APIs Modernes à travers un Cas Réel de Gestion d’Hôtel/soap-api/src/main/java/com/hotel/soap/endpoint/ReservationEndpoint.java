package com.hotel.soap.endpoint;

import com.hotel.common.dto.ReservationDTO;
import com.hotel.common.entity.Reservation.StatutReservation;
import com.hotel.common.service.ReservationService;
import com.hotel.soap.config.WebServiceConfig;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * SOAP Endpoint for Reservation operations.
 */
@Endpoint
@RequiredArgsConstructor
@Slf4j
public class ReservationEndpoint {

    private final ReservationService reservationService;
    private static final String NAMESPACE_URI = WebServiceConfig.NAMESPACE_URI;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllReservationsRequest")
    @ResponsePayload
    public JAXBElement<GetAllReservationsResponse> getAllReservations(
            @RequestPayload JAXBElement<GetAllReservationsRequest> request) {
        log.info("SOAP: Getting all reservations");
        
        List<ReservationDTO> reservations = reservationService.findAll();
        GetAllReservationsResponse response = new GetAllReservationsResponse();
        
        for (ReservationDTO dto : reservations) {
            response.getReservations().add(toSoapReservation(dto));
        }
        
        return createElement("getAllReservationsResponse", response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReservationByIdRequest")
    @ResponsePayload
    public JAXBElement<GetReservationByIdResponse> getReservationById(
            @RequestPayload JAXBElement<GetReservationByIdRequest> request) {
        Long id = request.getValue().getId();
        log.info("SOAP: Getting reservation by ID: {}", id);
        
        ReservationDTO dto = reservationService.findById(id);
        GetReservationByIdResponse response = new GetReservationByIdResponse();
        response.setReservation(toSoapReservation(dto));
        
        return createElement("getReservationByIdResponse", response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createReservationRequest")
    @ResponsePayload
    public JAXBElement<CreateReservationResponse> createReservation(
            @RequestPayload JAXBElement<CreateReservationRequest> request) {
        CreateReservationRequest req = request.getValue();
        log.info("SOAP: Creating reservation for client {} in room {}", req.getClientId(), req.getChambreId());
        
        ReservationDTO dto = ReservationDTO.builder()
                .clientId(req.getClientId())
                .chambreId(req.getChambreId())
                .dateDebut(toLocalDate(req.getDateDebut()))
                .dateFin(toLocalDate(req.getDateFin()))
                .preferences(req.getPreferences())
                .nombrePersonnes(req.getNombrePersonnes())
                .commentaires(req.getCommentaires())
                .build();
        
        ReservationDTO created = reservationService.create(dto);
        CreateReservationResponse response = new CreateReservationResponse();
        response.setReservation(toSoapReservation(created));
        
        return createElement("createReservationResponse", response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateReservationRequest")
    @ResponsePayload
    public JAXBElement<UpdateReservationResponse> updateReservation(
            @RequestPayload JAXBElement<UpdateReservationRequest> request) {
        UpdateReservationRequest req = request.getValue();
        log.info("SOAP: Updating reservation: {}", req.getId());
        
        ReservationDTO dto = ReservationDTO.builder()
                .clientId(req.getClientId())
                .chambreId(req.getChambreId())
                .dateDebut(toLocalDate(req.getDateDebut()))
                .dateFin(toLocalDate(req.getDateFin()))
                .preferences(req.getPreferences())
                .nombrePersonnes(req.getNombrePersonnes())
                .commentaires(req.getCommentaires())
                .build();
        
        ReservationDTO updated = reservationService.update(req.getId(), dto);
        UpdateReservationResponse response = new UpdateReservationResponse();
        response.setReservation(toSoapReservation(updated));
        
        return createElement("updateReservationResponse", response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteReservationRequest")
    @ResponsePayload
    public JAXBElement<DeleteReservationResponse> deleteReservation(
            @RequestPayload JAXBElement<DeleteReservationRequest> request) {
        Long id = request.getValue().getId();
        log.info("SOAP: Deleting reservation: {}", id);
        
        DeleteReservationResponse response = new DeleteReservationResponse();
        try {
            reservationService.delete(id);
            response.setSuccess(true);
            response.setMessage("Réservation supprimée avec succès");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        
        return createElement("deleteReservationResponse", response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReservationsByClientRequest")
    @ResponsePayload
    public JAXBElement<GetReservationsByClientResponse> getReservationsByClient(
            @RequestPayload JAXBElement<GetReservationsByClientRequest> request) {
        Long clientId = request.getValue().getClientId();
        log.info("SOAP: Getting reservations for client: {}", clientId);
        
        List<ReservationDTO> reservations = reservationService.findByClientId(clientId);
        GetReservationsByClientResponse response = new GetReservationsByClientResponse();
        
        for (ReservationDTO dto : reservations) {
            response.getReservations().add(toSoapReservation(dto));
        }
        
        return createElement("getReservationsByClientResponse", response);
    }

    // ==================== HELPER METHODS ====================

    private Reservation toSoapReservation(ReservationDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        reservation.setDateDebut(toXmlDate(dto.getDateDebut()));
        reservation.setDateFin(toXmlDate(dto.getDateFin()));
        reservation.setStatut(StatutReservationType.fromValue(dto.getStatut().name()));
        reservation.setPreferences(dto.getPreferences());
        reservation.setNombrePersonnes(dto.getNombrePersonnes());
        reservation.setPrixTotal(dto.getPrixTotal());
        reservation.setCommentaires(dto.getCommentaires());
        
        if (dto.getClient() != null) {
            Client client = new Client();
            client.setId(dto.getClient().getId());
            client.setNom(dto.getClient().getNom());
            client.setPrenom(dto.getClient().getPrenom());
            client.setEmail(dto.getClient().getEmail());
            client.setTelephone(dto.getClient().getTelephone());
            reservation.setClient(client);
        }
        
        if (dto.getChambre() != null) {
            Chambre chambre = new Chambre();
            chambre.setId(dto.getChambre().getId());
            chambre.setNumero(dto.getChambre().getNumero());
            chambre.setType(TypeChambreType.fromValue(dto.getChambre().getType().name()));
            chambre.setPrix(dto.getChambre().getPrix());
            chambre.setDisponible(dto.getChambre().getDisponible());
            chambre.setDescription(dto.getChambre().getDescription());
            chambre.setCapaciteMax(dto.getChambre().getCapaciteMax());
            reservation.setChambre(chambre);
        }
        
        return reservation;
    }

    private LocalDate toLocalDate(XMLGregorianCalendar xmlCal) {
        if (xmlCal == null) return null;
        return LocalDate.of(xmlCal.getYear(), xmlCal.getMonth(), xmlCal.getDay());
    }

    private XMLGregorianCalendar toXmlDate(LocalDate date) {
        if (date == null) return null;
        try {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error converting date", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> JAXBElement<T> createElement(String localPart, T value) {
        return new JAXBElement<>(
                new QName(NAMESPACE_URI, localPart),
                (Class<T>) value.getClass(),
                value
        );
    }

    // ==================== INNER CLASSES (Generated from XSD) ====================
    // These would normally be generated by JAXB from the XSD
    
    public static class GetAllReservationsRequest {}
    public static class GetAllReservationsResponse {
        private java.util.List<Reservation> reservations = new java.util.ArrayList<>();
        public java.util.List<Reservation> getReservations() { return reservations; }
    }
    
    public static class GetReservationByIdRequest {
        private Long id;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
    public static class GetReservationByIdResponse {
        private Reservation reservation;
        public Reservation getReservation() { return reservation; }
        public void setReservation(Reservation reservation) { this.reservation = reservation; }
    }
    
    public static class CreateReservationRequest {
        private Long clientId;
        private Long chambreId;
        private XMLGregorianCalendar dateDebut;
        private XMLGregorianCalendar dateFin;
        private String preferences;
        private Integer nombrePersonnes;
        private String commentaires;
        
        public Long getClientId() { return clientId; }
        public void setClientId(Long clientId) { this.clientId = clientId; }
        public Long getChambreId() { return chambreId; }
        public void setChambreId(Long chambreId) { this.chambreId = chambreId; }
        public XMLGregorianCalendar getDateDebut() { return dateDebut; }
        public void setDateDebut(XMLGregorianCalendar dateDebut) { this.dateDebut = dateDebut; }
        public XMLGregorianCalendar getDateFin() { return dateFin; }
        public void setDateFin(XMLGregorianCalendar dateFin) { this.dateFin = dateFin; }
        public String getPreferences() { return preferences; }
        public void setPreferences(String preferences) { this.preferences = preferences; }
        public Integer getNombrePersonnes() { return nombrePersonnes; }
        public void setNombrePersonnes(Integer nombrePersonnes) { this.nombrePersonnes = nombrePersonnes; }
        public String getCommentaires() { return commentaires; }
        public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    }
    public static class CreateReservationResponse {
        private Reservation reservation;
        public Reservation getReservation() { return reservation; }
        public void setReservation(Reservation reservation) { this.reservation = reservation; }
    }
    
    public static class UpdateReservationRequest extends CreateReservationRequest {
        private Long id;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
    public static class UpdateReservationResponse {
        private Reservation reservation;
        public Reservation getReservation() { return reservation; }
        public void setReservation(Reservation reservation) { this.reservation = reservation; }
    }
    
    public static class DeleteReservationRequest {
        private Long id;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
    public static class DeleteReservationResponse {
        private boolean success;
        private String message;
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class GetReservationsByClientRequest {
        private Long clientId;
        public Long getClientId() { return clientId; }
        public void setClientId(Long clientId) { this.clientId = clientId; }
    }
    public static class GetReservationsByClientResponse {
        private java.util.List<Reservation> reservations = new java.util.ArrayList<>();
        public java.util.List<Reservation> getReservations() { return reservations; }
    }
    
    // SOAP Types
    public static class Reservation {
        private Long id;
        private Client client;
        private Chambre chambre;
        private XMLGregorianCalendar dateDebut;
        private XMLGregorianCalendar dateFin;
        private StatutReservationType statut;
        private String preferences;
        private Integer nombrePersonnes;
        private BigDecimal prixTotal;
        private String commentaires;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Client getClient() { return client; }
        public void setClient(Client client) { this.client = client; }
        public Chambre getChambre() { return chambre; }
        public void setChambre(Chambre chambre) { this.chambre = chambre; }
        public XMLGregorianCalendar getDateDebut() { return dateDebut; }
        public void setDateDebut(XMLGregorianCalendar dateDebut) { this.dateDebut = dateDebut; }
        public XMLGregorianCalendar getDateFin() { return dateFin; }
        public void setDateFin(XMLGregorianCalendar dateFin) { this.dateFin = dateFin; }
        public StatutReservationType getStatut() { return statut; }
        public void setStatut(StatutReservationType statut) { this.statut = statut; }
        public String getPreferences() { return preferences; }
        public void setPreferences(String preferences) { this.preferences = preferences; }
        public Integer getNombrePersonnes() { return nombrePersonnes; }
        public void setNombrePersonnes(Integer nombrePersonnes) { this.nombrePersonnes = nombrePersonnes; }
        public BigDecimal getPrixTotal() { return prixTotal; }
        public void setPrixTotal(BigDecimal prixTotal) { this.prixTotal = prixTotal; }
        public String getCommentaires() { return commentaires; }
        public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    }
    
    public static class Client {
        private Long id;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String telephone) { this.telephone = telephone; }
    }
    
    public static class Chambre {
        private Long id;
        private String numero;
        private TypeChambreType type;
        private BigDecimal prix;
        private Boolean disponible;
        private String description;
        private Integer capaciteMax;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        public TypeChambreType getType() { return type; }
        public void setType(TypeChambreType type) { this.type = type; }
        public BigDecimal getPrix() { return prix; }
        public void setPrix(BigDecimal prix) { this.prix = prix; }
        public Boolean getDisponible() { return disponible; }
        public void setDisponible(Boolean disponible) { this.disponible = disponible; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Integer getCapaciteMax() { return capaciteMax; }
        public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }
    }
    
    public enum StatutReservationType {
        EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE;
        public static StatutReservationType fromValue(String v) { return valueOf(v); }
    }
    
    public enum TypeChambreType {
        SIMPLE, DOUBLE, SUITE, DELUXE, FAMILIALE;
        public static TypeChambreType fromValue(String v) { return valueOf(v); }
    }
}
