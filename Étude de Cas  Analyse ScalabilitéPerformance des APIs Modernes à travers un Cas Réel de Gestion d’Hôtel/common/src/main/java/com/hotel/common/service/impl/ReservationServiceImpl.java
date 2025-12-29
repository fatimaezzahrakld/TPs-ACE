package com.hotel.common.service.impl;

import com.hotel.common.dto.ReservationDTO;
import com.hotel.common.entity.Chambre;
import com.hotel.common.entity.Client;
import com.hotel.common.entity.Reservation;
import com.hotel.common.entity.Reservation.StatutReservation;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.BusinessException;
import com.hotel.common.mapper.EntityMapper;
import com.hotel.common.repository.ChambreRepository;
import com.hotel.common.repository.ClientRepository;
import com.hotel.common.repository.ReservationRepository;
import com.hotel.common.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of ReservationService.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findAll() {
        return mapper.toReservationDTOList(reservationRepository.findAllWithDetails());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDTO findById(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id);
        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation", "id", id);
        }
        return mapper.toReservationDTO(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findByClientId(Long clientId) {
        return mapper.toReservationDTOList(reservationRepository.findByClientId(clientId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findByChambreId(Long chambreId) {
        return mapper.toReservationDTOList(reservationRepository.findByChambreId(chambreId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findByStatus(StatutReservation statut) {
        return mapper.toReservationDTOList(reservationRepository.findByStatut(statut));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return mapper.toReservationDTOList(reservationRepository.findByDateRange(startDate, endDate));
    }

    @Override
    public ReservationDTO create(ReservationDTO reservationDTO) {
        // Validate dates
        if (reservationDTO.getDateDebut().isAfter(reservationDTO.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }
        
        if (reservationDTO.getDateDebut().isBefore(LocalDate.now())) {
            throw new BusinessException("La date de début ne peut pas être dans le passé");
        }

        // Fetch client and room
        Client client = clientRepository.findById(reservationDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", reservationDTO.getClientId()));
        
        Chambre chambre = chambreRepository.findById(reservationDTO.getChambreId())
                .orElseThrow(() -> new ResourceNotFoundException("Chambre", "id", reservationDTO.getChambreId()));

        // Check room availability
        if (!isRoomAvailable(chambre.getId(), reservationDTO.getDateDebut(), reservationDTO.getDateFin())) {
            throw new BusinessException("La chambre n'est pas disponible pour les dates sélectionnées");
        }

        Reservation reservation = mapper.toReservation(reservationDTO, client, chambre);
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        
        Reservation saved = reservationRepository.save(reservation);
        return mapper.toReservationDTO(saved);
    }

    @Override
    public ReservationDTO update(Long id, ReservationDTO reservationDTO) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Validate dates
        if (reservationDTO.getDateDebut().isAfter(reservationDTO.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }

        // Check if room is being changed or dates are changing
        boolean roomOrDatesChanged = !existing.getChambre().getId().equals(reservationDTO.getChambreId())
                || !existing.getDateDebut().equals(reservationDTO.getDateDebut())
                || !existing.getDateFin().equals(reservationDTO.getDateFin());

        if (roomOrDatesChanged) {
            // Check availability excluding current reservation
            List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                    reservationDTO.getChambreId(), 
                    reservationDTO.getDateDebut(), 
                    reservationDTO.getDateFin());
            overlapping.removeIf(r -> r.getId().equals(id));
            
            if (!overlapping.isEmpty()) {
                throw new BusinessException("La chambre n'est pas disponible pour les dates sélectionnées");
            }
        }

        // Update client and room if changed
        if (!existing.getClient().getId().equals(reservationDTO.getClientId())) {
            Client client = clientRepository.findById(reservationDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "id", reservationDTO.getClientId()));
            existing.setClient(client);
        }

        if (!existing.getChambre().getId().equals(reservationDTO.getChambreId())) {
            Chambre chambre = chambreRepository.findById(reservationDTO.getChambreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chambre", "id", reservationDTO.getChambreId()));
            existing.setChambre(chambre);
        }

        existing.setDateDebut(reservationDTO.getDateDebut());
        existing.setDateFin(reservationDTO.getDateFin());
        existing.setPreferences(reservationDTO.getPreferences());
        existing.setNombrePersonnes(reservationDTO.getNombrePersonnes());
        existing.setCommentaires(reservationDTO.getCommentaires());

        Reservation updated = reservationRepository.save(existing);
        return mapper.toReservationDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", "id", id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public ReservationDTO updateStatus(Long id, StatutReservation statut) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        reservation.setStatut(statut);
        Reservation updated = reservationRepository.save(reservation);
        return mapper.toReservationDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Long chambreId, LocalDate dateDebut, LocalDate dateFin) {
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(chambreId, dateDebut, dateFin);
        return overlapping.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findCurrentAndUpcoming() {
        return mapper.toReservationDTOList(reservationRepository.findCurrentAndUpcoming(LocalDate.now()));
    }
}
