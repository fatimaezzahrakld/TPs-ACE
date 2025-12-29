package com.hotel.common.service.impl;

import com.hotel.common.dto.ChambreDTO;
import com.hotel.common.entity.Chambre;
import com.hotel.common.entity.Chambre.TypeChambre;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.DuplicateResourceException;
import com.hotel.common.mapper.EntityMapper;
import com.hotel.common.repository.ChambreRepository;
import com.hotel.common.service.ChambreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of ChambreService.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChambreServiceImpl implements ChambreService {

    private final ChambreRepository chambreRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ChambreDTO> findAll() {
        return mapper.toChambreDTOList(chambreRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ChambreDTO findById(Long id) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chambre", "id", id));
        return mapper.toChambreDTO(chambre);
    }

    @Override
    @Transactional(readOnly = true)
    public ChambreDTO findByNumero(String numero) {
        Chambre chambre = chambreRepository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Chambre", "numero", numero));
        return mapper.toChambreDTO(chambre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChambreDTO> findAvailable() {
        return mapper.toChambreDTOList(chambreRepository.findByDisponibleTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChambreDTO> findByType(TypeChambre type) {
        return mapper.toChambreDTOList(chambreRepository.findByType(type));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChambreDTO> findByPriceRange(BigDecimal minPrix, BigDecimal maxPrix) {
        return mapper.toChambreDTOList(chambreRepository.findByPrixBetween(minPrix, maxPrix));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChambreDTO> findAvailableForDates(LocalDate dateDebut, LocalDate dateFin) {
        return mapper.toChambreDTOList(chambreRepository.findAvailableRooms(dateDebut, dateFin));
    }

    @Override
    public ChambreDTO create(ChambreDTO chambreDTO) {
        if (chambreRepository.findByNumero(chambreDTO.getNumero()).isPresent()) {
            throw new DuplicateResourceException("Chambre", "numero", chambreDTO.getNumero());
        }
        Chambre chambre = mapper.toChambre(chambreDTO);
        Chambre saved = chambreRepository.save(chambre);
        return mapper.toChambreDTO(saved);
    }

    @Override
    public ChambreDTO update(Long id, ChambreDTO chambreDTO) {
        Chambre existing = chambreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chambre", "id", id));
        
        // Check if numero is being changed and new numero already exists
        if (!existing.getNumero().equals(chambreDTO.getNumero()) 
                && chambreRepository.findByNumero(chambreDTO.getNumero()).isPresent()) {
            throw new DuplicateResourceException("Chambre", "numero", chambreDTO.getNumero());
        }
        
        existing.setNumero(chambreDTO.getNumero());
        existing.setType(chambreDTO.getType());
        existing.setPrix(chambreDTO.getPrix());
        existing.setDisponible(chambreDTO.getDisponible());
        existing.setDescription(chambreDTO.getDescription());
        existing.setCapaciteMax(chambreDTO.getCapaciteMax());
        existing.setEquipements(chambreDTO.getEquipements());
        
        Chambre updated = chambreRepository.save(existing);
        return mapper.toChambreDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!chambreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chambre", "id", id);
        }
        chambreRepository.deleteById(id);
    }

    @Override
    public void updateAvailability(Long id, boolean disponible) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chambre", "id", id));
        chambre.setDisponible(disponible);
        chambreRepository.save(chambre);
    }
}
