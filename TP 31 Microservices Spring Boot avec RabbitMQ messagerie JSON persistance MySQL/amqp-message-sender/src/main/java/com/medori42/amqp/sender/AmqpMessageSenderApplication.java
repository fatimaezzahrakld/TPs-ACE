package com.fatimaezzahrakld.amqp.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the AMQP Message Sender Service.
 *
 * <p>
 * This Spring Boot application provides REST APIs for publishing messages to
 * RabbitMQ.
 * It serves as a producer in an event-driven microservices architecture.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class AmqpMessageSenderApplication {

    private static final Logger logger = LoggerFactory.getLogger(AmqpMessageSenderApplication.class);

    /**
     * Main entry point for the Spring Boot application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AmqpMessageSenderApplication.class, args);
        logger.info("AMQP Message Sender Service started successfully.");
    }
}
