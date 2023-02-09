package nl.tudelft.sem.contract.client;

import static nl.tudelft.sem.contract.client.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import nl.tudelft.sem.contract.client.util.ResponseException;
import nl.tudelft.sem.contract.commons.ApiError;
import nl.tudelft.sem.contract.commons.entities.ActionSuccessDto;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContractDataTest {
    private MockWebServer mockServer;
    private final transient ObjectMapper objectMapper = new ObjectMapper();
    private ContractClient client;

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setUp() throws IOException {
        this.mockServer = new MockWebServer();
        this.mockServer.start();
        this.client = new ContractClient(new ContractClientConfiguration(mockServer.url("/").uri()));
    }


    @Test
    void getExistingContract() throws JsonProcessingException, InterruptedException {
        ContractDto contract = new ContractDto();
        contract.setId(getUuid(1));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(contract))
        );

        ContractDto returnedContract = client.contract().getContract(getUuid(1)).join();
        assertEquals(returnedContract.getId(), contract.getId());
        assertTrue(Objects.requireNonNull(mockServer.takeRequest().getPath()).endsWith("/contract/" + getUuid(1)));
    }



    @Test
    void getNonExistingContract() throws JsonProcessingException {
        ApiError err = new ApiError();
        err.setStatus(404);
        err.setDescription("Resource not found");
        mockServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(err))
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> client.contract().getContract(getUuid(1)).join());
        assertInstanceOf(ResponseException.class, actualException.getCause());
        assertEquals(404, ((ResponseException) actualException.getCause()).error().getStatus());
    }

    @Test
    void getNullContract() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> client.contract().getContract(null));
    }

    @Test
    void modifyNullContract() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> client.contract().modifyContract(getUuid(1), null));
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class,
                () -> client.contract().modifyContract(null, new ContractModificationDto()));
    }

    @Test
    void modifyNonExistingContract() throws JsonProcessingException {
        ApiError err = new ApiError();
        err.setStatus(404);
        err.setDescription("Resource not found");
        mockServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(err))
        );

        ContractModificationDto contract = new ContractModificationDto();

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> client.contract().modifyContract(getUuid(1), contract).join());
        assertInstanceOf(ResponseException.class, actualException.getCause());
        assertEquals(404, ((ResponseException) actualException.getCause()).error().getStatus());
    }

    @Test
    void modifyExistingContract() throws JsonProcessingException, InterruptedException {
        ContractDto expectedContract = new ContractDto();
        expectedContract.setId(getUuid(1));

        ContractModificationDto contract = new ContractModificationDto();
        contract.setBenefits(List.of("benefit1", "benefit2"));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(expectedContract))
        );

        ContractDto result = client.contract().modifyContract(getUuid(1), contract).join();
        assertEquals(expectedContract, result);
        assertTrue(Objects.requireNonNull(mockServer.takeRequest().getPath()).endsWith("/contract/" + getUuid(1)));
    }

    @Test
    void terminateNonExistingContract() throws JsonProcessingException {
        ApiError err = new ApiError();
        err.setStatus(404);
        err.setDescription("Resource not found");
        mockServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(err))
        );

        CompletionException actualException = assertThrows(CompletionException.class,
                () -> client.contract().terminateContract(getUuid(1)).join());
        assertInstanceOf(ResponseException.class, actualException.getCause());
        assertEquals(404, ((ResponseException) actualException.getCause()).error().getStatus());
    }

    @Test
    void terminateExistingContract() throws JsonProcessingException, InterruptedException {
        ActionSuccessDto result = new ActionSuccessDto();
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(result))
        );

        ActionSuccessDto actualResult = client.contract().terminateContract(getUuid(1)).join();
        assertEquals(result, actualResult);

        RecordedRequest request = mockServer.takeRequest();
        assertTrue(Objects.requireNonNull(request.getPath()).endsWith("/contract/terminate/" + getUuid(1)));
        assertEquals("DELETE", Objects.requireNonNull(request.getMethod()));
    }
}