package com.hotel.common.repository;

import com.hotel.common.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Client entity operations.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Find client by email.
     */
    Optional<Client> findByEmail(String email);

    /**
     * Check if email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Find clients by name (partial match).
     */
    @Query("SELECT c FROM Client c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%')) OR LOWER(c.prenom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Client> findByNomContaining(@Param("nom") String nom);

    /**
     * Find clients with reservations.
     */
    @Query("SELECT DISTINCT c FROM Client c JOIN FETCH c.reservations")
    List<Client> findAllWithReservations();
}
