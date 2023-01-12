package nl.tudelft.sem.user.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.Test;


class UserClientTest {
    @Test
    void instantiateClientProperly() {
        UserClient client = new UserClient(new UserClientConfiguration(URI.create("http://localhost:8080")));
        assertNotNull(client);
    }

    @Test
    void instantiateClientWithNullConfiguration() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> new UserClient(null));
    }
}