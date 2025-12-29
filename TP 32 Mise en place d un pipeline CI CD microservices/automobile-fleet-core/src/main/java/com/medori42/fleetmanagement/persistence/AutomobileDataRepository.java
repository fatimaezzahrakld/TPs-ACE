package com.fatimaezzahrakld.fleetmanagement.persistence;

import com.fatimaezzahrakld.fleetmanagement.domain.AutomobileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing automobile data.
 *
 * <p>
 * Provides standard CRUD operations and can be extended with custom queries.
 * Uses Spring Data JPA for automatic implementation generation.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see AutomobileEntity
 * @see JpaRepository
 */
@Repository
public interface AutomobileDataRepository extends JpaRepository<AutomobileEntity, Long> {

    // Standard CRUD methods are provided automatically by JpaRepository
}
