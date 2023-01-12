package nl.tudelft.sem.contract.client;

import java.net.URI;
import lombok.Getter;
import lombok.NonNull;

/**
 * Configuration for the contract client.
 */
@Getter
public class ContractClientConfiguration {
    /**
     * The base URI of the contract microservice.
     */
    @NonNull
    private URI baseUri;

    public ContractClientConfiguration(@NonNull URI baseUri) {
        this.baseUri = baseUri;
    }

    public static ContractClientConfigurationBuilder builder() {
        return new ContractClientConfigurationBuilder();
    }

    public static class ContractClientConfigurationBuilder {
        private transient @NonNull URI baseUriForm;

        ContractClientConfigurationBuilder() {
        }

        public ContractClientConfigurationBuilder baseUri(@NonNull URI baseUri) {
            this.baseUriForm = baseUri;
            return this;
        }

        public ContractClientConfiguration build() {
            return new ContractClientConfiguration(baseUriForm);
        }
    }
}
