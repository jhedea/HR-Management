package nl.tudelft.sem.template.authentication.domain.providers.implementations;

import java.net.URI;
import nl.tudelft.sem.user.client.UserClient;
import nl.tudelft.sem.user.client.UserClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ClientProvider {
    @Bean
    public UserClient userClient() {
        return new UserClient(new UserClientConfiguration(URI.create("http://localhost:8082")));
    }
}

