package nl.tudelft.sem.request.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.request.commons.entities.RequestDto;
import nl.tudelft.sem.request.commons.entities.utils.IdDto;
import nl.tudelft.sem.request.commons.entities.utils.StringDto;

@RequiredArgsConstructor
public class RequestData {

    private final transient RequestClient client;

    public CompletableFuture<RequestDto> createRequest(@NonNull RequestDto requestDto)
            throws JsonProcessingException {
        return client.post("/request", requestDto, RequestDto.class);
    }

    public CompletableFuture<RequestDto> modifyContractRequest(@NonNull UUID id, @NonNull IdDto idDto)
            throws JsonProcessingException {
        return client.post("/request/" + id, idDto, RequestDto.class);
    }

    public CompletableFuture<RequestDto> getRequest(@NonNull UUID requestId) {
        return client.get("/request/" + requestId, RequestDto.class);
    }

    public CompletableFuture<RequestDto> rejectRequest(@NonNull UUID id, @NonNull StringDto responseBody)
            throws JsonProcessingException {
        return client.put("/request/reject/" + id, responseBody, RequestDto.class);
    }

    public CompletableFuture<RequestDto> approveRequest(@NonNull UUID id, @NonNull IdDto idDto)
            throws JsonProcessingException {
        return client.put("/request/approve/" + id, idDto, RequestDto.class);
    }


}
