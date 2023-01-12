package nl.tudelft.sem.contract.microservice.services;

import java.net.URI;
import nl.tudelft.sem.notification.client.NotificationClient;
import nl.tudelft.sem.notification.client.NotificationClientConfiguration;
import nl.tudelft.sem.user.client.UserClient;
import nl.tudelft.sem.user.client.UserClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ClientProvider {
    /**
     * Build a new notification client.
     *
     * @return a notification client.
     */
    @Bean
    public NotificationClient getNotificationClient() {
        return new NotificationClient(NotificationClientConfiguration.builder()
                .baseUri(URI.create("http://localhost:8085"))
                .build());
    }

    @Bean
    public UserClient userClient() {
        return new UserClient(new UserClientConfiguration(URI.create("http://localhost:8082")));
    }
}
