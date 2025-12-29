package com.hotel.rest.controller;

import com.hotel.common.dto.ReservationDTO;
import com.hotel.common.entity.Reservation.StatutReservation;
import com.hotel.common.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Reservation operations.
 */
@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "API pour la gestion des réservations")
@CrossOrigin(origins = "*")
public class ReservationRestController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réservation par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
        @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    public ResponseEntity<ReservationDTO> getReservationById(
            @Parameter(description = "ID de la réservation") @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle réservation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<ReservationDTO> createReservation(
            @Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO created = reservationService.create(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une réservation existante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Réservation modifiée avec succès"),
        @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    public ResponseEntity<ReservationDTO> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(reservationService.update(id, reservationDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer les réservations d'un client")
    public ResponseEntity<List<ReservationDTO>> getReservationsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(reservationService.findByClientId(clientId));
    }

    @GetMapping("/chambre/{chambreId}")
    @Operation(summary = "Récupérer les réservations d'une chambre")
    public ResponseEntity<List<ReservationDTO>> getReservationsByChambre(@PathVariable Long chambreId) {
        return ResponseEntity.ok(reservationService.findByChambreId(chambreId));
    }

    @GetMapping("/status/{statut}")
    @Operation(summary = "Récupérer les réservations par statut")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatus(@PathVariable StatutReservation statut) {
        return ResponseEntity.ok(reservationService.findByStatus(statut));
    }

    @GetMapping("/dates")
    @Operation(summary = "Récupérer les réservations dans une période")
    public ResponseEntity<List<ReservationDTO>> getReservationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reservationService.findByDateRange(startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Modifier le statut d'une réservation")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam StatutReservation statut) {
        return ResponseEntity.ok(reservationService.updateStatus(id, statut));
    }

    @GetMapping("/availability")
    @Operation(summary = "Vérifier la disponibilité d'une chambre")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long chambreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(reservationService.isRoomAvailable(chambreId, dateDebut, dateFin));
    }

    @GetMapping("/current")
    @Operation(summary = "Récupérer les réservations en cours et à venir")
    public ResponseEntity<List<ReservationDTO>> getCurrentAndUpcoming() {
        return ResponseEntity.ok(reservationService.findCurrentAndUpcoming());
    }
}
