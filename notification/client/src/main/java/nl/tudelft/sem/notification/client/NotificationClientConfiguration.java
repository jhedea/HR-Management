package nl.tudelft.sem.notification.client;

import java.net.URI;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Configuration for the contract client.
 */
@Builder
@Getter
public class NotificationClientConfiguration {
    /**
     * The base URI of the contract microservice.
     */
    @NonNull
    private URI baseUri;
}
