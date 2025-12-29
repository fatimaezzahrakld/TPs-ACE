package com.example.bookservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA Entity representing a Book in the library system.
 *
 * <p>
 * This class defines a book with its title, author, and current stock level.
 * It is mapped to the 'book' table in the database.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * Unique identifier for the book.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the book.
     * Must be unique and not null.
     */
    @Column(nullable = false, unique = true, length = 120)
    private String title;

    /**
     * Author of the book.
     * Must not be null.
     */
    @Column(nullable = false, length = 80)
    private String author;

    /**
     * Current stock level of the book.
     * Must not be null.
     */
    @Column(nullable = false)
    private int stock;

    /**
     * Constructor for creating a new book with essential details.
     *
     * @param title  Title of the book
     * @param author Author of the book
     * @param stock  Initial stock level
     */
    public Book(String title, String author, int stock) {
        this.title = title;
        this.author = author;
        this.stock = stock;
    }

    /**
     * Decrements the stock level by one.
     *
     * @throws IllegalStateException If the book is out of stock
     */
    public void decrementStock() {
        if (stock <= 0) {
            throw new IllegalStateException("Out of stock");
        }
        stock--;
    }
}
