package com.example.bookservice.repo;

import com.example.bookservice.domain.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for the Book entity.
 *
 * <p>
 * This interface provides standard database operations (CRUD) and
 * custom queries for the Book entity, including pessimistic locking
 * for concurrent updates.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds a book by its title.
     *
     * @param title Title of the book
     * @return An Optional containing the book if found
     */
    Optional<Book> findByTitle(String title);

    /**
     * Finds a book by its ID and applies a pessimistic write lock.
     *
     * <p>
     * This is used to prevent concurrent modifications to the same book
     * record during transactional operations like borrowing.
     * </p>
     *
     * @param id Identifier of the book
     * @return An Optional containing the book if found
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") Long id);
}
