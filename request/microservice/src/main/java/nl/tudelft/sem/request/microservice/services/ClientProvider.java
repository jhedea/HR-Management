package nl.tudelft.sem.request.microservice.services;

import java.net.URI;
import nl.tudelft.sem.contract.client.ContractClient;
import nl.tudelft.sem.contract.client.ContractClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ClientProvider {
    @Bean
    public ContractClient contractClient() {
        return new ContractClient(new ContractClientConfiguration(URI.create("http://localhost:8083")));
    }
}