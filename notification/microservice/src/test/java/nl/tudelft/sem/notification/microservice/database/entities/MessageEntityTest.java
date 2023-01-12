package nl.tudelft.sem.notification.microservice.database.entities;

import static nl.tudelft.sem.notification.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.commons.entities.MessagePriority;
import org.junit.jupiter.api.Test;

class MessageEntityTest {
    @Test
    void testGetDto() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSubject("subject");
        messageEntity.setMessage("message");
        messageEntity.setSent(true);
        messageEntity.setUserId(getUuid(1));
        messageEntity.setPriority(MessagePriority.HIGH);
        messageEntity.setId(getUuid(2));

        MessageDto messageDto = messageEntity.getDto();

        assertEquals("subject", messageDto.getSubject());
        assertEquals("message", messageDto.getMessage());
        assertTrue(messageDto.isSent());
        assertEquals(getUuid(1), messageDto.getUserId());
        assertEquals(MessagePriority.HIGH, messageDto.getPriority());
        assertEquals(getUuid(2), messageDto.getId());
    }

    @Test
    void testEquals() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSubject("subject");
        messageEntity.setMessage("message");
        messageEntity.setSent(true);
        messageEntity.setUserId(getUuid(1));
        messageEntity.setPriority(MessagePriority.HIGH);
        messageEntity.setId(getUuid(2));

        MessageEntity messageEntity2 = new MessageEntity();
        messageEntity2.setSubject("subject");
        messageEntity2.setMessage("message");
        messageEntity2.setSent(true);
        messageEntity2.setUserId(getUuid(1));
        messageEntity2.setPriority(MessagePriority.HIGH);
        messageEntity2.setId(getUuid(2));

        assertEquals(messageEntity, messageEntity2);
    }

    @Test
    void testEqualsSameObject() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSubject("subject");
        messageEntity.setMessage("message");
        messageEntity.setSent(true);
        messageEntity.setUserId(getUuid(1));
        messageEntity.setPriority(MessagePriority.HIGH);
        messageEntity.setId(getUuid(2));

        assertEquals(messageEntity, messageEntity);
    }

    @Test
    void testEqualsNull() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSubject("subject");
        messageEntity.setMessage("message");
        messageEntity.setSent(true);
        messageEntity.setUserId(getUuid(1));
        messageEntity.setPriority(MessagePriority.HIGH);
        messageEntity.setId(getUuid(2));

        assertNotEquals(messageEntity, null);
    }
}