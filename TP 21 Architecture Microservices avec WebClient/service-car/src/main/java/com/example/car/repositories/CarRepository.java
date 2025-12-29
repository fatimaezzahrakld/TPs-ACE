package com.example.car.repositories;

import com.example.car.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for the Car entity.
 *
 * <p>
 * This interface extends JpaRepository to provide standard database
 * operations (CRUD) on the Car entity. It also includes a custom
 * finder method to retrieve cars by client ID.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Finds all cars belonging to a specific client.
     *
     * @param clientId The unique identifier of the client
     * @return List of cars owned by the client
     */
    List<Car> findByClientId(Long clientId);
}
