package nl.tudelft.sem.notification.microservice.controllers;

import static nl.tudelft.sem.notification.microservice.TestHelpers.getUuid;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import nl.tudelft.sem.notification.commons.entities.MessageCollectionDto;
import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.commons.entities.MessagePriority;
import nl.tudelft.sem.notification.commons.entities.UuidCollectionDto;
import nl.tudelft.sem.notification.commons.entities.UuidDto;
import nl.tudelft.sem.notification.microservice.services.MessageDispatcher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ActiveProfiles("testing")
class NotificationControllerTest {
    private final transient MockMvc mockMvc;
    private final transient ObjectMapper objectMapper;

    @MockBean
    private transient MessageDispatcher messageDispatcher;

    @Captor
    private ArgumentCaptor<MessageDto> messageCaptor;
    @Captor
    private ArgumentCaptor<List<MessageDto>> messageListCaptor;

    @Autowired
    NotificationControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper().registerModules(new Jdk8Module(), new JavaTimeModule());
    }

    private MessageDto getMessage(int id, MessagePriority priority) {
        MessageDto messageDto = new MessageDto();
        messageDto.setUserId(getUuid(id));
        messageDto.setMessage("message" + id);
        messageDto.setPriority(priority);
        messageDto.setSent(false);
        messageDto.setSubject("subject" + id);
        return messageDto;
    }

    @Test
    void sendMessage() throws Exception {
        MessageDto message = new MessageDto();
        message.setUserId(getUuid(2));
        message.setMessage("Test message");
        message.setSent(false);
        message.setPriority(MessagePriority.LOW);
        message.setSubject("subject text");

        when(messageDispatcher.addMessageToQueue(messageCaptor.capture())).thenReturn(getUuid(3));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/notification/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new UuidDto(getUuid(3)))));
        verify(messageDispatcher, times(1)).addMessageToQueue(messageCaptor.getValue());
    }

    @Test
    void sendMultipleMessages() throws Exception {
        MessageDto message1 = getMessage(1, MessagePriority.LOW);
        MessageDto message2 = getMessage(2, MessagePriority.HIGH);

        MessageCollectionDto messageCollectionDto = new MessageCollectionDto(List.of(message1, message2));

        when(messageDispatcher.addMessagesToQueue(messageListCaptor.capture())).thenReturn(List.of(getUuid(1), getUuid(2)));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/notification/sendBatch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageCollectionDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new UuidCollectionDto(
                        List.of(getUuid(1), getUuid(2))))));
        verify(messageDispatcher, times(1)).addMessagesToQueue(messageListCaptor.getValue());
    }
}