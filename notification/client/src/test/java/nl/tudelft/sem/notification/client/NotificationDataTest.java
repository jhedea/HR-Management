package nl.tudelft.sem.notification.client;

import static nl.tudelft.sem.notification.client.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.commons.entities.UuidCollectionDto;
import nl.tudelft.sem.notification.commons.entities.UuidDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationDataTest {
    private MockWebServer mockServer;
    private final transient ObjectMapper objectMapper = new ObjectMapper();
    private NotificationClient client;

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setUp() throws IOException {
        this.mockServer = new MockWebServer();
        this.mockServer.start();
        this.client = new NotificationClient(
                NotificationClientConfiguration.builder().baseUri(mockServer.url("/").uri()).build());
    }


    @Test
    void sendMessage() throws JsonProcessingException, InterruptedException {
        UuidDto expectedUuid = new UuidDto(getUuid(3));

        MessageDto msg = new MessageDto();
        msg.setUserId(getUuid(1));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(expectedUuid))
        );

        UuidDto returnedUuid = client.notification().sendMessage(msg).join();
        assertEquals(expectedUuid, returnedUuid);
        assertTrue(Objects.requireNonNull(mockServer.takeRequest().getPath()).endsWith("/send"));
    }

    @Test
    void sendNullMessage() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> client.notification().sendMessage(null).join());
    }

    @Test
    void sendMessages() throws JsonProcessingException, InterruptedException {
        UuidCollectionDto expectedUuids = new UuidCollectionDto(List.of(getUuid(3), getUuid(4)));

        MessageDto msg1 = new MessageDto();
        msg1.setUserId(getUuid(1));
        MessageDto msg2 = new MessageDto();
        msg2.setUserId(getUuid(2));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(expectedUuids))
        );

        UuidCollectionDto returnedUuids = client.notification().sendMessages(List.of(msg1, msg2)).join();
        assertEquals(expectedUuids, returnedUuids);
        assertTrue(Objects.requireNonNull(mockServer.takeRequest().getPath()).endsWith("/sendBatch"));
    }

    @Test
    void sendNullMessages() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> client.notification().sendMessages(null).join());
    }
}