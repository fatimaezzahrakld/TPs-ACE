package com.hotel.common.service;

import com.hotel.common.dto.ChambreDTO;
import com.hotel.common.entity.Chambre.TypeChambre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Chambre operations.
 */
public interface ChambreService {
    
    List<ChambreDTO> findAll();
    
    ChambreDTO findById(Long id);
    
    ChambreDTO findByNumero(String numero);
    
    List<ChambreDTO> findAvailable();
    
    List<ChambreDTO> findByType(TypeChambre type);
    
    List<ChambreDTO> findByPriceRange(BigDecimal minPrix, BigDecimal maxPrix);
    
    List<ChambreDTO> findAvailableForDates(LocalDate dateDebut, LocalDate dateFin);
    
    ChambreDTO create(ChambreDTO chambreDTO);
    
    ChambreDTO update(Long id, ChambreDTO chambreDTO);
    
    void delete(Long id);
    
    void updateAvailability(Long id, boolean disponible);
}
