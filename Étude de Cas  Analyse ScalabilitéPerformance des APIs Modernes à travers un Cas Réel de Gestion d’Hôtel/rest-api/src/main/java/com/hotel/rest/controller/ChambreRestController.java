package com.hotel.rest.controller;

import com.hotel.common.dto.ChambreDTO;
import com.hotel.common.entity.Chambre.TypeChambre;
import com.hotel.common.service.ChambreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Chambre operations.
 */
@RestController
@RequestMapping("/api/chambres")
@Tag(name = "Chambres", description = "API pour la gestion des chambres")
@CrossOrigin(origins = "*")
public class ChambreRestController {

    private final ChambreService chambreService;

    @Autowired
    public ChambreRestController(ChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les chambres")
    @ApiResponse(responseCode = "200", description = "Liste des chambres récupérée avec succès")
    public ResponseEntity<List<ChambreDTO>> getAllChambres() {
        return ResponseEntity.ok(chambreService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une chambre par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chambre trouvée"),
        @ApiResponse(responseCode = "404", description = "Chambre non trouvée")
    })
    public ResponseEntity<ChambreDTO> getChambreById(@PathVariable Long id) {
        return ResponseEntity.ok(chambreService.findById(id));
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Récupérer une chambre par numéro")
    public ResponseEntity<ChambreDTO> getChambreByNumero(@PathVariable String numero) {
        return ResponseEntity.ok(chambreService.findByNumero(numero));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle chambre")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Chambre créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<ChambreDTO> createChambre(@Valid @RequestBody ChambreDTO chambreDTO) {
        ChambreDTO created = chambreService.create(chambreDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une chambre existante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chambre modifiée avec succès"),
        @ApiResponse(responseCode = "404", description = "Chambre non trouvée")
    })
    public ResponseEntity<ChambreDTO> updateChambre(
            @PathVariable Long id,
            @Valid @RequestBody ChambreDTO chambreDTO) {
        return ResponseEntity.ok(chambreService.update(id, chambreDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une chambre")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Chambre supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Chambre non trouvée")
    })
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        chambreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    @Operation(summary = "Récupérer les chambres disponibles")
    public ResponseEntity<List<ChambreDTO>> getAvailableChambres() {
        return ResponseEntity.ok(chambreService.findAvailable());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Récupérer les chambres par type")
    public ResponseEntity<List<ChambreDTO>> getChambresByType(@PathVariable TypeChambre type) {
        return ResponseEntity.ok(chambreService.findByType(type));
    }

    @GetMapping("/price-range")
    @Operation(summary = "Récupérer les chambres par fourchette de prix")
    public ResponseEntity<List<ChambreDTO>> getChambresByPriceRange(
            @RequestParam BigDecimal minPrix,
            @RequestParam BigDecimal maxPrix) {
        return ResponseEntity.ok(chambreService.findByPriceRange(minPrix, maxPrix));
    }

    @GetMapping("/available-dates")
    @Operation(summary = "Récupérer les chambres disponibles pour des dates")
    public ResponseEntity<List<ChambreDTO>> getAvailableChambresForDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(chambreService.findAvailableForDates(dateDebut, dateFin));
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Modifier la disponibilité d'une chambre")
    public ResponseEntity<Void> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean disponible) {
        chambreService.updateAvailability(id, disponible);
        return ResponseEntity.ok().build();
    }
}
