package com.hotel.common.service;

import com.hotel.common.dto.ReservationDTO;
import com.hotel.common.entity.Reservation.StatutReservation;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Reservation operations.
 */
public interface ReservationService {
    
    List<ReservationDTO> findAll();
    
    ReservationDTO findById(Long id);
    
    List<ReservationDTO> findByClientId(Long clientId);
    
    List<ReservationDTO> findByChambreId(Long chambreId);
    
    List<ReservationDTO> findByStatus(StatutReservation statut);
    
    List<ReservationDTO> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    ReservationDTO create(ReservationDTO reservationDTO);
    
    ReservationDTO update(Long id, ReservationDTO reservationDTO);
    
    void delete(Long id);
    
    ReservationDTO updateStatus(Long id, StatutReservation statut);
    
    boolean isRoomAvailable(Long chambreId, LocalDate dateDebut, LocalDate dateFin);
    
    List<ReservationDTO> findCurrentAndUpcoming();
}
