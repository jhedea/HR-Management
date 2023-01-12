package nl.tudelft.sem.notification.microservice.utils;

import java.util.UUID;
import lombok.Data;
import nl.tudelft.sem.notification.commons.entities.MessagePriority;
import nl.tudelft.sem.notification.microservice.database.entities.MessageEntity;

/**
 * Represents a message queue entry.
 */
@Data
public class MessageQueueEntry implements Comparable<MessageQueueEntry> {
    /** The unique identifier for the message. */
    private UUID id;

    /** The priority of the message. */
    private int priority;

    /**
     * Constructs a new `MessageQueueEntry` instance.
     *
     * @param messageEntity the `MessageEntity` to create the `MessageQueueEntry` from
     */
    public MessageQueueEntry(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.priority = messageEntity.getPriority().getValue();
    }

    /**
     * Constructs a new `MessageQueueEntry` instance.
     *
     * @param messageId the message id
     * @param priority the priority
     */
    public MessageQueueEntry(UUID messageId, int priority) {
        this.id = messageId;
        this.priority = priority;
    }

    /**
     * Constructs a new `MessageQueueEntry` instance.
     *
     * @param messageId the message id
     * @param priority the priority
     */
    public MessageQueueEntry(UUID messageId, MessagePriority priority) {
        this(messageId, priority.getValue());
    }

    @Override
    public int compareTo(MessageQueueEntry messageQueueEntry) {
        return this.priority - messageQueueEntry.getPriority();
    }
}