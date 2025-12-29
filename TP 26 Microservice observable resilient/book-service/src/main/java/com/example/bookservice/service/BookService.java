package com.example.bookservice.service;

import com.example.bookservice.domain.Book;
import com.example.bookservice.repo.BookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing Book operations.
 *
 * <p>
 * This class provides business logic for book management, including
 * retrieving all books, creating new ones, and handling the borrowing process
 * with stock management and price retrieval.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repo;
    private final PricingClient pricing;

    /**
     * Constructor for BookService with required dependencies.
     *
     * @param repo    The book repository
     * @param pricing The pricing client for external price retrieval
     */
    public BookService(BookRepository repo, PricingClient pricing) {
        this.repo = repo;
        this.pricing = pricing;
    }

    /**
     * Retrieves all books from the database.
     *
     * @return List of all books
     */
    public List<Book> all() {
        logger.info("Fetching all books from the database.");
        return repo.findAll();
    }

    /**
     * Creates a new book record.
     *
     * @param b The book to create
     * @return The saved book
     * @throws IllegalArgumentException If a book with the same title already exists
     */
    public Book create(Book b) {
        logger.info("Creating new book: {}", b.getTitle());
        repo.findByTitle(b.getTitle()).ifPresent(x -> {
            logger.error("Attempted to create book with existing title: {}", b.getTitle());
            throw new IllegalArgumentException("Title already exists");
        });
        return repo.save(b);
    }

    /**
     * Handles the borrowing of a book by its identifier.
     *
     * <p>
     * This method is transactional and applies a pessimistic lock on the
     * book record to ensure stock consistency during concurrent updates.
     * </p>
     *
     * @param id Identifier of the book to borrow
     * @return A BorrowResult containing book details and the retrieved price
     * @throws IllegalArgumentException If the book is not found
     * @throws IllegalStateException    If the book is out of stock
     */
    @Transactional
    public BorrowResult borrow(long id) {
        logger.info("Processing borrow request for book ID: {}", id);

        // Apply database lock for update
        Book book = repo.findByIdForUpdate(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new IllegalArgumentException("Book not found");
                });

        book.decrementStock(); // May throw IllegalStateException if stock is 0

        logger.info("Fetching price for book ID: {}", id);
        double price = pricing.getPrice(id);

        logger.info("Borrow successful for book: {}. Stock left: {}", book.getTitle(), book.getStock());
        return new BorrowResult(book.getId(), book.getTitle(), book.getStock(), price);
    }

    /**
     * Record representing the result of a borrow operation.
     *
     * @param id        Identifier of the book
     * @param title     Title of the book
     * @param stockLeft Remaining stock level
     * @param price     Retrieved price for the book
     */
    public record BorrowResult(Long id, String title, int stockLeft, double price) {
    }
}
