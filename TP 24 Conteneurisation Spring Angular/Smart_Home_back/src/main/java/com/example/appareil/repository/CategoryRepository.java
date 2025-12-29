package com.example.appareil.repository;

import com.example.appareil.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Category entity.
 *
 * <p>
 * This interface extends JpaRepository to provide standard database
 * operations (CRUD) on the Category entity.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
