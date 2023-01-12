package nl.tudelft.sem.notification.microservice.services;

import static nl.tudelft.sem.notification.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.commons.entities.MessagePriority;
import nl.tudelft.sem.notification.microservice.database.entities.MessageEntity;
import nl.tudelft.sem.notification.microservice.database.repositories.MessageEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MessageDispatcherTest {
    @Mock
    private MessageEntityRepository messageEntityRepository;

    @Mock
    private JavaMailSender mailSender;

    private MessageDispatcher messageDispatcher;

    @BeforeEach
    void setUp() {
        messageDispatcher = new MessageDispatcher(messageEntityRepository, mailSender);
    }

    MessageDto getMessageDto(int id, MessagePriority priority) {
        MessageDto message = new MessageDto();
        message.setPriority(priority);
        message.setSubject("subject" + id);
        message.setMessage("message" + id);
        message.setSent(false);
        message.setUserId(getUuid(id));
        return message;
    }

    @Test
    void addMessageToQueue() {
        MessageDto messageDto = getMessageDto(1, MessagePriority.HIGH);

        MessageEntity messageEntity = new MessageEntity(messageDto);
        messageEntity.setId(getUuid(2));

        when(messageEntityRepository.save(any(MessageEntity.class))).thenReturn(messageEntity);

        assertEquals(getUuid(2), messageDispatcher.addMessageToQueue(messageDto));
        assertEquals(1, messageDispatcher.getQueueSize());
    }

    @Test
    void addNullMessageToQueue() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> messageDispatcher.addMessageToQueue(null));
        assertEquals(0, messageDispatcher.getQueueSize());
    }

    @Test
    void addMessagesToQueue() {
        MessageDto messageDto1 = getMessageDto(1, MessagePriority.HIGH);
        MessageDto messageDto2 = getMessageDto(2, MessagePriority.HIGH);

        MessageEntity messageEntity1 = new MessageEntity(messageDto1);
        MessageEntity messageEntity2 = new MessageEntity(messageDto2);
        messageEntity1.setId(getUuid(1));
        messageEntity2.setId(getUuid(2));

        when(messageEntityRepository.saveAll(any())).thenReturn(List.of(messageEntity1, messageEntity2));

        assertEquals(List.of(getUuid(1), getUuid(2)),
                messageDispatcher.addMessagesToQueue(List.of(messageDto1, messageDto2)));
        assertEquals(2, messageDispatcher.getQueueSize());
    }

    @Test
    void addNullMessagesToQueue() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> messageDispatcher.addMessagesToQueue(null));
        assertEquals(0, messageDispatcher.getQueueSize());
    }

    @Test
    void sendMessages() {
        MessageDto messageDto1 = getMessageDto(1, MessagePriority.HIGH);
        MessageDto messageDto2 = getMessageDto(2, MessagePriority.LOW);

        MessageEntity messageEntity1 = new MessageEntity(messageDto1);
        MessageEntity messageEntity2 = new MessageEntity(messageDto2);
        messageEntity1.setId(getUuid(1));
        messageEntity2.setId(getUuid(2));

        when(messageEntityRepository.saveAll(any())).thenReturn(List.of(messageEntity1, messageEntity2));
        messageDispatcher.addMessagesToQueue(List.of(messageDto1, messageDto2));
        assertEquals(2, messageDispatcher.getQueueSize());

        when(messageEntityRepository.findById(getUuid(1))).thenReturn(Optional.of(messageEntity1));
        when(messageEntityRepository.findById(getUuid(2))).thenReturn(Optional.of(messageEntity2));

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(messageEntityRepository.save(any())).thenReturn(null);

        messageDispatcher.sendMessages();

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
        assertEquals(0, messageDispatcher.getQueueSize());
    }

    @Test
    void sendMessagesNullReturn() {
        MessageDto messageDto1 = getMessageDto(1, MessagePriority.HIGH);
        MessageEntity messageEntity1 = new MessageEntity(messageDto1);
        messageEntity1.setId(getUuid(1));

        when(messageEntityRepository.saveAll(any())).thenReturn(List.of(messageEntity1));
        messageDispatcher.addMessagesToQueue(List.of(messageDto1));
        assertEquals(1, messageDispatcher.getQueueSize());

        when(messageEntityRepository.findById(getUuid(1))).thenReturn(Optional.empty());
        messageDispatcher.sendMessages();
        assertEquals(0, messageDispatcher.getQueueSize());
    }

    @Test
    void sendMessagesThrows() {
        MessageDto messageDto1 = getMessageDto(1, MessagePriority.HIGH);
        MessageEntity messageEntity1 = new MessageEntity(messageDto1);
        messageEntity1.setId(getUuid(1));

        when(messageEntityRepository.saveAll(any())).thenReturn(List.of(messageEntity1));
        messageDispatcher.addMessagesToQueue(List.of(messageDto1));
        assertEquals(1, messageDispatcher.getQueueSize());

        when(messageEntityRepository.findById(getUuid(1))).thenReturn(Optional.of(messageEntity1));
        doThrow(new RuntimeException()).when(mailSender).send(any(SimpleMailMessage.class));
        messageDispatcher.sendMessages();
        assertEquals(0, messageDispatcher.getQueueSize());
    }
}