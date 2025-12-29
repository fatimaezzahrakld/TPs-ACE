package com.hotel.common.service;

import com.hotel.common.dto.ClientDTO;

import java.util.List;

/**
 * Service interface for Client operations.
 */
public interface ClientService {
    
    List<ClientDTO> findAll();
    
    ClientDTO findById(Long id);
    
    ClientDTO findByEmail(String email);
    
    ClientDTO create(ClientDTO clientDTO);
    
    ClientDTO update(Long id, ClientDTO clientDTO);
    
    void delete(Long id);
    
    List<ClientDTO> search(String nom);
    
    boolean existsByEmail(String email);
}
