package com.example.client_service.repositories;

import com.example.client_service.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Client entity.
 *
 * <p>This interface extends JpaRepository to provide standard database
 * operations (CRUD) on the Client entity. Spring Data JPA automatically
 * generates the implementation at runtime.</p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
