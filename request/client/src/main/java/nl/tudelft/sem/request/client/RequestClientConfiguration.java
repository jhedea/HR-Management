package nl.tudelft.sem.request.client;

import java.net.URI;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class RequestClientConfiguration {
    /**
     * The base URI of the request microservice.
     */
    @NonNull
    private URI baseUri;
}
