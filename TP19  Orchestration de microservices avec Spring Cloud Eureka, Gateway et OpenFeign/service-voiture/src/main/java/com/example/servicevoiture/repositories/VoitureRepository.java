package com.example.servicevoiture.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.servicevoiture.entities.Voiture;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    List<Voiture> findById_client(Long id_client);
}
