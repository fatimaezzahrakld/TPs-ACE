package com.hotel.rest.controller;

import com.hotel.common.dto.ClientDTO;
import com.hotel.common.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Client operations.
 */
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "API pour la gestion des clients")
@CrossOrigin(origins = "*")
public class ClientRestController {

    private final ClientService clientService;

    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les clients")
    @ApiResponse(responseCode = "200", description = "Liste des clients récupérée avec succès")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client trouvé"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Récupérer un client par email")
    public ResponseEntity<ClientDTO> getClientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.findByEmail(email));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau client")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO created = clientService.create(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client existant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client modifié avec succès"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.update(id, clientDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Client supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des clients par nom")
    public ResponseEntity<List<ClientDTO>> searchClients(@RequestParam String nom) {
        return ResponseEntity.ok(clientService.search(nom));
    }

    @GetMapping("/exists")
    @Operation(summary = "Vérifier si un email existe")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return ResponseEntity.ok(clientService.existsByEmail(email));
    }
}
