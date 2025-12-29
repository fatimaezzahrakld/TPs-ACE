package com.fatimaezzahrakld.amqp.sender.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration class for the AMQP Message Sender Service.
 *
 * <p>
 * Configures exchanges, queues, bindings, and message converters required
 * for asynchronous communication using RabbitMQ.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    /**
     * Name of the exchange for user messages.
     */
    @Value("${rabbitmq.exchange.name:user.exchange}")
    private String exchangeName;

    /**
     * Name of the queue for user messages.
     */
    @Value("${rabbitmq.queue.name:user.queue}")
    private String queueName;

    /**
     * Routing key for user messages.
     */
    @Value("${rabbitmq.routing.key:user.routingkey}")
    private String routingKey;

    /**
     * Creates a direct exchange for routing user messages.
     *
     * @return DirectExchange instance
     */
    @Bean
    public DirectExchange userExchange() {
        logger.info("Initializing DirectExchange: {}", exchangeName);
        return new DirectExchange(exchangeName);
    }

    /**
     * Creates a durable queue for user messages.
     *
     * @return Queue instance
     */
    @Bean
    public Queue userQueue() {
        logger.info("Initializing Queue: {}", queueName);
        return QueueBuilder.durable(queueName).build();
    }

    /**
     * Creates a binding between the queue and exchange using the routing key.
     *
     * @param userQueue    The queue to bind
     * @param userExchange The exchange to bind to
     * @return Binding instance
     */
    @Bean
    public Binding userBinding(Queue userQueue, DirectExchange userExchange) {
        logger.info("Binding Queue '{}' to Exchange '{}' with Routing Key '{}'", queueName, exchangeName, routingKey);
        return BindingBuilder.bind(userQueue).to(userExchange).with(routingKey);
    }

    /**
     * Creates a Jackson JSON message converter for serializing messages.
     *
     * @return MessageConverter instance using Jackson for JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        logger.info("Initializing Jackson2JsonMessageConverter.");
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a RabbitTemplate configured with JSON message conversion.
     *
     * @param connectionFactory The connection factory for RabbitMQ
     * @return RabbitTemplate instance
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        logger.info("Initializing RabbitTemplate with JSON message converter.");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
