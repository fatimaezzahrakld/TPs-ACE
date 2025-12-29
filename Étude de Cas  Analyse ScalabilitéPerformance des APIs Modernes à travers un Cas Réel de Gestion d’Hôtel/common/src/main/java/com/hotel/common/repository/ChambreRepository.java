package com.hotel.common.repository;

import com.hotel.common.entity.Chambre;
import com.hotel.common.entity.Chambre.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Chambre entity operations.
 */
@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {

    /**
     * Find room by number.
     */
    Optional<Chambre> findByNumero(String numero);

    /**
     * Find available rooms.
     */
    List<Chambre> findByDisponibleTrue();

    /**
     * Find rooms by type.
     */
    List<Chambre> findByType(TypeChambre type);

    /**
     * Find rooms by price range.
     */
    List<Chambre> findByPrixBetween(BigDecimal minPrix, BigDecimal maxPrix);

    /**
     * Find available rooms by type.
     */
    List<Chambre> findByTypeAndDisponibleTrue(TypeChambre type);

    /**
     * Find rooms available for specific dates.
     */
    @Query("SELECT c FROM Chambre c WHERE c.disponible = true AND c.id NOT IN " +
           "(SELECT r.chambre.id FROM Reservation r WHERE " +
           "(r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut) AND r.statut != 'ANNULEE')")
    List<Chambre> findAvailableRooms(@Param("dateDebut") LocalDate dateDebut, 
                                      @Param("dateFin") LocalDate dateFin);

    /**
     * Find rooms with capacity.
     */
    List<Chambre> findByCapaciteMaxGreaterThanEqual(Integer capacite);
}
