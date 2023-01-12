package nl.tudelft.sem.request.client;

import static nl.tudelft.sem.request.client.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import nl.tudelft.sem.request.client.util.ResponseException;
import nl.tudelft.sem.request.commons.ApiError;
import nl.tudelft.sem.request.commons.entities.RequestDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RequestDataTest {
    private MockWebServer mockServer;
    private final transient ObjectMapper objectMapper = new ObjectMapper();
    private RequestClient requestClient;

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setUp() throws IOException {
        this.mockServer = new MockWebServer();
        this.mockServer.start();
        this.requestClient = new RequestClient(new RequestClientConfiguration(mockServer.url("/").uri()));
    }

    @Test
    void getExistingRequest() throws JsonProcessingException, InterruptedException {
        RequestDto request = new RequestDto();
        request.setId(getUuid(1));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(request))
        );

        RequestDto returnedRequest = requestClient.requestData().getRequest(getUuid(1)).join();
        assertEquals(returnedRequest.getId(), request.getId());
        assertTrue(Objects.requireNonNull(mockServer.takeRequest().getPath()).endsWith("/request/" + getUuid(1)));
    }

    @Test
    void getNonExistingRequest() throws JsonProcessingException {
        ApiError err = new ApiError();
        err.setStatus(404);
        err.setDescription("Resource not found");
        mockServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(err))
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> requestClient.requestData().getRequest(getUuid(1)).join());
        assertInstanceOf(ResponseException.class, actualException.getCause());
        assertEquals(404, ((ResponseException) actualException.getCause()).error().getStatus());
    }

    @Test
    void getNullRequest() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> requestClient.requestData().getRequest(null));
    }


}
