package com.hotel.common.service.impl;

import com.hotel.common.dto.ClientDTO;
import com.hotel.common.entity.Client;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.DuplicateResourceException;
import com.hotel.common.mapper.EntityMapper;
import com.hotel.common.repository.ClientRepository;
import com.hotel.common.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of ClientService.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return mapper.toClientDTOList(clientRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        return mapper.toClientDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));
        return mapper.toClientDTO(client);
    }

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        if (clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new DuplicateResourceException("Client", "email", clientDTO.getEmail());
        }
        Client client = mapper.toClient(clientDTO);
        Client saved = clientRepository.save(client);
        return mapper.toClientDTO(saved);
    }

    @Override
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        
        // Check if email is being changed and if new email already exists
        if (!existing.getEmail().equals(clientDTO.getEmail()) 
                && clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new DuplicateResourceException("Client", "email", clientDTO.getEmail());
        }
        
        existing.setNom(clientDTO.getNom());
        existing.setPrenom(clientDTO.getPrenom());
        existing.setEmail(clientDTO.getEmail());
        existing.setTelephone(clientDTO.getTelephone());
        
        Client updated = clientRepository.save(existing);
        return mapper.toClientDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", "id", id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> search(String nom) {
        return mapper.toClientDTOList(clientRepository.findByNomContaining(nom));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }
}
