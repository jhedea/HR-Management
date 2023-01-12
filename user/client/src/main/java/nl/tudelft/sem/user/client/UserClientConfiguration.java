package nl.tudelft.sem.user.client;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;


/**
 * Configuration for the user client.
 */
@Getter
@AllArgsConstructor
public class UserClientConfiguration {
    /**
     * The base URI of the user microservice.
     */
    @NonNull
    private transient URI baseUri;

    public static UserClientConfigurationBuilder builder() {
        return new UserClientConfigurationBuilder();
    }

    public static class UserClientConfigurationBuilder {
        private transient URI uri;

        private UserClientConfigurationBuilder() {
        }

        public UserClientConfigurationBuilder baseUri(@NonNull URI baseUri) {
            this.uri = baseUri;
            return this;
        }

        public UserClientConfiguration build() {
            return new UserClientConfiguration(uri);
        }
    }
}
