package com.example.bookservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Book Service.
 *
 * <p>
 * This microservice manages a library of books and provides a REST API
 * for book operations. It integrates with a pricing service and uses
 * resilience patterns for fault tolerance.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class BookServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(BookServiceApplication.class);

	/**
	 * Main entry point for the Book Service application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
		logger.info("Book Service started successfully.");
	}

}
