package com.hotel.rest.config;

import com.hotel.common.entity.Chambre;
import com.hotel.common.entity.Chambre.TypeChambre;
import com.hotel.common.entity.Client;
import com.hotel.common.entity.Reservation;
import com.hotel.common.entity.Reservation.StatutReservation;
import com.hotel.common.repository.ChambreRepository;
import com.hotel.common.repository.ClientRepository;
import com.hotel.common.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Data initializer to populate database with sample data.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Profile("!test")
    public CommandLineRunner initData(
            ClientRepository clientRepository,
            ChambreRepository chambreRepository,
            ReservationRepository reservationRepository) {
        
        return args -> {
            // Only initialize if database is empty
            if (clientRepository.count() > 0) {
                log.info("Database already initialized, skipping data initialization");
                return;
            }

            log.info("Initializing database with sample data...");

            // Create Clients
            List<Client> clients = Arrays.asList(
                createClient("Dupont", "Jean", "jean.dupont@email.com", "+33612345678"),
                createClient("Martin", "Marie", "marie.martin@email.com", "+33623456789"),
                createClient("Bernard", "Pierre", "pierre.bernard@email.com", "+33634567890"),
                createClient("Petit", "Sophie", "sophie.petit@email.com", "+33645678901"),
                createClient("Robert", "Thomas", "thomas.robert@email.com", "+33656789012"),
                createClient("Richard", "Emma", "emma.richard@email.com", "+33667890123"),
                createClient("Durand", "Lucas", "lucas.durand@email.com", "+33678901234"),
                createClient("Leroy", "Léa", "lea.leroy@email.com", "+33689012345"),
                createClient("Moreau", "Hugo", "hugo.moreau@email.com", "+33690123456"),
                createClient("Simon", "Chloé", "chloe.simon@email.com", "+33601234567")
            );
            clientRepository.saveAll(clients);
            log.info("Created {} clients", clients.size());

            // Create Chambres
            List<Chambre> chambres = Arrays.asList(
                createChambre("101", TypeChambre.SIMPLE, "89.00", true, "Chambre simple avec vue sur le jardin", 1, Arrays.asList("WiFi", "TV", "Climatisation")),
                createChambre("102", TypeChambre.SIMPLE, "89.00", true, "Chambre simple confortable", 1, Arrays.asList("WiFi", "TV", "Climatisation")),
                createChambre("201", TypeChambre.DOUBLE, "129.00", true, "Chambre double avec balcon", 2, Arrays.asList("WiFi", "TV", "Climatisation", "Mini-bar", "Balcon")),
                createChambre("202", TypeChambre.DOUBLE, "129.00", true, "Chambre double vue mer", 2, Arrays.asList("WiFi", "TV", "Climatisation", "Mini-bar", "Vue mer")),
                createChambre("301", TypeChambre.SUITE, "249.00", true, "Suite luxueuse avec salon séparé", 3, Arrays.asList("WiFi", "TV 55\"", "Climatisation", "Mini-bar", "Jacuzzi", "Salon")),
                createChambre("302", TypeChambre.SUITE, "279.00", true, "Suite présidentielle", 4, Arrays.asList("WiFi", "TV 65\"", "Climatisation", "Mini-bar", "Jacuzzi", "Terrasse", "Bureau")),
                createChambre("401", TypeChambre.DELUXE, "189.00", true, "Chambre deluxe moderne", 2, Arrays.asList("WiFi", "TV 50\"", "Climatisation", "Mini-bar", "Coffre-fort")),
                createChambre("501", TypeChambre.FAMILIALE, "199.00", true, "Chambre familiale spacieuse", 5, Arrays.asList("WiFi", "TV", "Climatisation", "Mini-bar", "Lit bébé disponible")),
                createChambre("502", TypeChambre.FAMILIALE, "219.00", true, "Grande chambre familiale avec cuisine", 6, Arrays.asList("WiFi", "TV", "Climatisation", "Cuisine", "Machine à laver")),
                createChambre("103", TypeChambre.SIMPLE, "79.00", true, "Chambre simple économique", 1, Arrays.asList("WiFi", "TV"))
            );
            chambreRepository.saveAll(chambres);
            log.info("Created {} chambres", chambres.size());

            // Create Reservations
            LocalDate today = LocalDate.now();
            List<Reservation> reservations = Arrays.asList(
                createReservation(clients.get(0), chambres.get(0), today.plusDays(1), today.plusDays(4), StatutReservation.CONFIRMEE, "Chambre non-fumeur, étage élevé", 1, "Client régulier"),
                createReservation(clients.get(1), chambres.get(2), today.plusDays(2), today.plusDays(7), StatutReservation.CONFIRMEE, "Petit-déjeuner inclus", 2, null),
                createReservation(clients.get(2), chambres.get(4), today.plusDays(5), today.plusDays(10), StatutReservation.EN_ATTENTE, "Anniversaire de mariage", 2, "Demande de champagne à l'arrivée"),
                createReservation(clients.get(3), chambres.get(7), today.plusDays(10), today.plusDays(17), StatutReservation.CONFIRMEE, "Lit bébé nécessaire", 4, "Famille avec enfants"),
                createReservation(clients.get(4), chambres.get(6), today.plusDays(3), today.plusDays(5), StatutReservation.CONFIRMEE, "Voyage d'affaires", 1, "Départ tôt le matin"),
                createReservation(clients.get(5), chambres.get(3), today.minusDays(2), today.plusDays(1), StatutReservation.TERMINEE, "Vue mer demandée", 2, null),
                createReservation(clients.get(6), chambres.get(5), today.plusDays(15), today.plusDays(22), StatutReservation.EN_ATTENTE, "Séjour long", 3, "Réduction demandée pour séjour prolongé"),
                createReservation(clients.get(7), chambres.get(1), today.plusDays(7), today.plusDays(9), StatutReservation.CONFIRMEE, "Arrivée tardive", 1, null)
            );
            reservationRepository.saveAll(reservations);
            log.info("Created {} reservations", reservations.size());

            log.info("Database initialization completed successfully!");
        };
    }

    private Client createClient(String nom, String prenom, String email, String telephone) {
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setTelephone(telephone);
        return client;
    }

    private Chambre createChambre(String numero, TypeChambre type, String prix, boolean disponible, String description, int capaciteMax, List<String> equipements) {
        Chambre chambre = new Chambre();
        chambre.setNumero(numero);
        chambre.setType(type);
        chambre.setPrix(new BigDecimal(prix));
        chambre.setDisponible(disponible);
        chambre.setDescription(description);
        chambre.setCapaciteMax(capaciteMax);
        chambre.setEquipements(equipements);
        return chambre;
    }

    private Reservation createReservation(Client client, Chambre chambre, LocalDate dateDebut, LocalDate dateFin, StatutReservation statut, String preferences, int nombrePersonnes, String commentaires) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setStatut(statut);
        reservation.setPreferences(preferences);
        reservation.setNombrePersonnes(nombrePersonnes);
        reservation.setCommentaires(commentaires);
        return reservation;
    }
}
