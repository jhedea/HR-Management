package nl.tudelft.sem.notification.microservice.utils;

import static nl.tudelft.sem.notification.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.notification.commons.entities.MessagePriority;
import nl.tudelft.sem.notification.microservice.database.entities.MessageEntity;
import org.junit.jupiter.api.Test;

class MessageQueueEntryTest {
    @Test
    void testComparator() {
        MessageQueueEntry entry1 = new MessageQueueEntry(getUuid(1), MessagePriority.LOW);
        MessageQueueEntry entry2 = new MessageQueueEntry(getUuid(1), MessagePriority.HIGH);
        assertTrue(entry1.compareTo(entry2) > 0);
    }

    @Test
    void testComparatorEqual() {
        MessageQueueEntry entry1 = new MessageQueueEntry(getUuid(1), MessagePriority.LOW);
        MessageQueueEntry entry2 = new MessageQueueEntry(getUuid(1), MessagePriority.LOW);
        assertEquals(0, entry1.compareTo(entry2));
    }

    @Test
    void testEntityConstructor() {
        MessageEntity entity = new MessageEntity(getUuid(1), "Subject", "Message", false, MessagePriority.LOW);
        MessageQueueEntry entry = new MessageQueueEntry(entity);
        assertEquals(entity.getId(), entry.getId());
    }
}