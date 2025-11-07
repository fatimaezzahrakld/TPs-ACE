package ma.ws.jaxrs.repositories;

import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.entities.TypeCompte;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    List<Compte> findByType(TypeCompte type);
    List<Compte> findBySoldeGreaterThan(double minSolde);
    List<Compte> findByDateCreationBetween(String startDate, String endDate);
}