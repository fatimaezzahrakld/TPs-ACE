package ma.projet.grpc.services;

import ma.projet.grpc.entities.Compte;
import ma.projet.grpc.repositories.CompteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompteService {
    private final CompteRepository compteRepository;
    
    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }
    
    public List<Compte> findAllComptes() {
        return compteRepository.findAll();
    }
    
    public Compte findCompteById(String id) {
        return compteRepository.findById(id).orElse(null);
    }
    
    public Compte saveCompte(Compte compte) {
        if (compte.getId() == null || compte.getId().isEmpty()) {
            compte.setId(UUID.randomUUID().toString());
        }
        return compteRepository.save(compte);
    }
}
