package com.example.bookservice.web;

import com.example.bookservice.domain.Book;
import com.example.bookservice.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book management.
 *
 * <p>
 * This class exposes REST endpoints for book operations, including
 * listing all books, creating new ones, and borrowing books.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService service;

    /**
     * Constructor for BookController with required dependencies.
     *
     * @param service The book service
     */
    public BookController(BookService service) {
        this.service = service;
    }

    /**
     * Retrieves all books.
     *
     * @return List of all books
     */
    @GetMapping
    public List<Book> all() {
        logger.info("REST request to fetch all books.");
        return service.all();
    }

    /**
     * Creates a new book record.
     *
     * @param b The book to create
     * @return The saved book
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book b) {
        logger.info("REST request to create new book: {}", b.getTitle());
        return service.create(b);
    }

    /**
     * Processes a borrow request for a specific book.
     *
     * @param id Identifier of the book to borrow
     * @return The result of the borrow operation
     */
    @PostMapping("/{id}/borrow")
    public BookService.BorrowResult borrow(@PathVariable long id) {
        logger.info("REST request to borrow book with ID: {}", id);
        return service.borrow(id);
    }
}
