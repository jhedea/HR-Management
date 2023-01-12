package nl.tudelft.sem.request.client.util;

import static nl.tudelft.sem.request.client.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.concurrent.CompletionException;
import nl.tudelft.sem.request.client.RequestClient;
import nl.tudelft.sem.request.client.RequestClientConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RequestFutureCallbackTest {
    public static MockWebServer mockServer;
    private RequestClient requestClient;

    @BeforeAll
    static void setup() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @BeforeEach
    void setUp() {
        this.requestClient = new RequestClient(RequestClientConfiguration.builder()
                .baseUri(mockServer.url("/").uri()).build());
    }

    @Test
    void invalidErrorResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(502)
                .setBody("Task failed successfully.")
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> requestClient.requestData().getRequest(getUuid(1)).join());
        assertInstanceOf(UnexpectedResponseException.class, actualException.getCause());
        assertEquals(502, ((UnexpectedResponseException) actualException.getCause()).statusCode());
    }

    @Test
    void invalidSuccessResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("Task failed with even more success. ")
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> requestClient.requestData().getRequest(getUuid(1)).join());
        assertInstanceOf(UnexpectedResponseException.class, actualException.getCause());
        assertEquals(200, ((UnexpectedResponseException) actualException.getCause()).statusCode());
    }
}
