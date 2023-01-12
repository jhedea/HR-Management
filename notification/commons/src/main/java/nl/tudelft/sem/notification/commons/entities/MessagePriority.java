package nl.tudelft.sem.notification.commons.entities;

import lombok.Getter;

/**
 * The enum Message priority.
 */
public enum MessagePriority {
    /**
     * Low message priority.
     */
    LOW(3),
    /**
     * Medium message priority.
     */
    MEDIUM(2),
    /**
     * High message priority.
     */
    HIGH(1),
    /**
     * Critical message priority.
     */
    REALTIME(0);

    @Getter
    private final int value;

    MessagePriority(int value) {
        this.value = value;
    }
}
