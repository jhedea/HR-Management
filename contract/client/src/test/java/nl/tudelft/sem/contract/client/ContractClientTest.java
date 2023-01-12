package nl.tudelft.sem.contract.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.Test;

class ContractClientTest {
    @Test
    void instantiateClientProperly() {
        ContractClient client = new ContractClient(new ContractClientConfiguration(URI.create("http://localhost:8080")));
        assertNotNull(client);
    }

    @Test
    void instantiateClientWithNullConfiguration() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> new ContractClient(null));
    }
}