package com.fatimaezzahrakld.amqp.sender.business;

import com.fatimaezzahrakld.amqp.sender.entity.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service class for sending messages to RabbitMQ.
 * Handles message preparation and publishing to the configured exchange.
 *
 * @author fatimaezzahrakld
 * @version 1.0.0
 */
@Service
public class MessageSenderService {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name:user.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:user.routingkey}")
    private String routingKey;

    /**
     * Constructor-based dependency injection for RabbitTemplate.
     *
     * @param rabbitTemplate the RabbitMQ template for sending messages
     */
    public MessageSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Sends a user message to RabbitMQ.
     * Enriches the message with a unique ID and timestamp before sending.
     *
     * @param userMessage the user message to send
     * @return the enriched message that was sent
     */
    public UserMessage sendMessage(UserMessage userMessage) {
        // Enrich message with metadata
        userMessage.setMessageId(UUID.randomUUID().toString());
        userMessage.setTimestamp(LocalDateTime.now());

        logger.info("Sending message to exchange '{}' with routing key '{}': {}",
                exchangeName, routingKey, userMessage);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, userMessage);

        logger.info("Message sent successfully with ID: {}", userMessage.getMessageId());

        return userMessage;
    }
}
