package com.fatimaezzahrakld.amqp.sender.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity class representing a user message to be sent via AMQP.
 * Contains user identification and message content with timestamp.
 *
 * @author fatimaezzahrakld
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     */
    private String userId;

    /**
     * Name of the user sending the message.
     */
    private String userName;

    /**
     * Content of the message.
     */
    private String messageContent;

    /**
     * Timestamp when the message was created.
     */
    private LocalDateTime timestamp;

    /**
     * Unique message identifier.
     */
    private String messageId;
}
