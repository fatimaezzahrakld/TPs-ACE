package com.fatimaezzahrakld.amqp.sender.api;

import com.fatimaezzahrakld.amqp.sender.business.MessageSenderService;
import com.fatimaezzahrakld.amqp.sender.entity.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling message sending requests.
 * Provides endpoints for publishing messages to RabbitMQ.
 *
 * @author fatimaezzahrakld
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageSenderService messageSenderService;

    /**
     * Constructor-based dependency injection for MessageSenderService.
     *
     * @param messageSenderService the service for sending messages
     */
    public MessageController(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    /**
     * Endpoint for publishing a user message to RabbitMQ.
     *
     * @param userMessage the message to publish
     * @return ResponseEntity containing the sent message with metadata
     */
    @PostMapping("/produce")
    public ResponseEntity<UserMessage> produceMessage(@RequestBody UserMessage userMessage) {
        logger.info("Received request to produce message for user: {}", userMessage.getUserName());

        UserMessage sentMessage = messageSenderService.sendMessage(userMessage);

        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }

    /**
     * Health check endpoint.
     *
     * @return ResponseEntity with status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AMQP Message Sender Service is running");
    }
}
