package nl.tudelft.sem.user.client.util;

import static nl.tudelft.sem.user.client.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.concurrent.CompletionException;
import nl.tudelft.sem.user.client.UserClient;
import nl.tudelft.sem.user.client.UserClientConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserFutureCallbackTest {
    public static MockWebServer mockServer;
    private UserClient client;

    @BeforeAll
    static void setup() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @BeforeEach
    void setUp() {
        this.client = new UserClient(UserClientConfiguration.builder()
                .baseUri(mockServer.url("/").uri()).build());
    }

    @Test
    void invalidErrorResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(502)
                .setBody("Something wrong happened")
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> client.user().getuser(getUuid(1)).join());
        assertInstanceOf(UnexpectedResponseException.class, actualException.getCause());
        assertEquals(502, ((UnexpectedResponseException) actualException.getCause()).statusCode());
    }

    @Test
    void invalidSuccessResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("Something catastrophically wrong happened")
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> client.user().getuser(getUuid(1)).join());
        assertInstanceOf(UnexpectedResponseException.class, actualException.getCause());
        assertEquals(200, ((UnexpectedResponseException) actualException.getCause()).statusCode());
    }
}