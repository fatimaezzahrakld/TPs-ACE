package com.example.car.repositories;

import com.example.car.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Car entity.
 *
 * <p>
 * This interface extends JpaRepository to provide standard database
 * operations (CRUD) on the Car entity. Spring Data JPA automatically
 * generates the implementation at runtime.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
