package com.hotel.common.repository;

import com.hotel.common.entity.Reservation;
import com.hotel.common.entity.Reservation.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Reservation entity operations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Find reservations by client ID.
     */
    List<Reservation> findByClientId(Long clientId);

    /**
     * Find reservations by room ID.
     */
    List<Reservation> findByChambreId(Long chambreId);

    /**
     * Find reservations by status.
     */
    List<Reservation> findByStatut(StatutReservation statut);

    /**
     * Find reservations by date range.
     */
    @Query("SELECT r FROM Reservation r WHERE r.dateDebut >= :startDate AND r.dateFin <= :endDate")
    List<Reservation> findByDateRange(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);

    /**
     * Find overlapping reservations for a room.
     */
    @Query("SELECT r FROM Reservation r WHERE r.chambre.id = :chambreId " +
           "AND r.statut != 'ANNULEE' " +
           "AND r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut")
    List<Reservation> findOverlappingReservations(@Param("chambreId") Long chambreId,
                                                   @Param("dateDebut") LocalDate dateDebut,
                                                   @Param("dateFin") LocalDate dateFin);

    /**
     * Find all reservations with client and room details.
     */
    @Query("SELECT r FROM Reservation r JOIN FETCH r.client JOIN FETCH r.chambre")
    List<Reservation> findAllWithDetails();

    /**
     * Find reservation by ID with details.
     */
    @Query("SELECT r FROM Reservation r JOIN FETCH r.client JOIN FETCH r.chambre WHERE r.id = :id")
    Reservation findByIdWithDetails(@Param("id") Long id);

    /**
     * Count reservations by status.
     */
    long countByStatut(StatutReservation statut);

    /**
     * Find current and upcoming reservations.
     */
    @Query("SELECT r FROM Reservation r WHERE r.dateFin >= :today AND r.statut != 'ANNULEE' ORDER BY r.dateDebut")
    List<Reservation> findCurrentAndUpcoming(@Param("today") LocalDate today);
}
