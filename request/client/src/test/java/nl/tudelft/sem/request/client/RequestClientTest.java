package nl.tudelft.sem.request.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.Test;


public class RequestClientTest {

    @Test
    void instantiateClient() {
        RequestClient requestClient = new RequestClient(new RequestClientConfiguration(URI.create("http://localhost:8084")));
        assertNotNull(requestClient);
    }

    @Test
    void instantiateClientNullConfiguration() {
        assertThrows(NullPointerException.class, () -> new RequestClient(null));
    }
}
