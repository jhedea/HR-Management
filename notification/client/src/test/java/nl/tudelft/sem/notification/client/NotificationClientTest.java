package nl.tudelft.sem.notification.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.Test;

class NotificationClientTest {
    @Test
    void instantiateClientProperly() {
        NotificationClient client = new NotificationClient(
                NotificationClientConfiguration.builder().baseUri(URI.create("http://localhost:8080")).build());
        assertNotNull(client);
    }

    @Test
    void instantiateClientWithNullConfiguration() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> new NotificationClient(null));
    }
}