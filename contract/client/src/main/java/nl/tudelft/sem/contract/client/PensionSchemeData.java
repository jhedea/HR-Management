package nl.tudelft.sem.contract.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.contract.commons.entities.PensionSchemeDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;



@RequiredArgsConstructor
public class PensionSchemeData {
    private static final String startPath = "/pension-scheme/";

    @NonNull
    private final transient ContractClient client;

    public CompletableFuture<PensionSchemeDto> getPensionScheme(@NonNull UUID pensionSchemeId) {
        return client.get(startPath + pensionSchemeId, PensionSchemeDto.class);
    }

    public CompletableFuture<PensionSchemeDto> addPensionScheme(@NonNull PensionSchemeDto pensionSchemeDto)
            throws JsonProcessingException {
        return client.post("/pension-scheme", pensionSchemeDto, PensionSchemeDto.class);
    }

    public CompletableFuture<PensionSchemeDto> deletePensionScheme(@NonNull UUID pensionSchemeId) {
        return client.delete(startPath + pensionSchemeId, PensionSchemeDto.class);
    }

    public CompletableFuture<PensionSchemeDto> editName(@NonNull UUID pensionSchemeId, @NonNull StringDto name)
            throws JsonProcessingException {
        return client.put(startPath + pensionSchemeId + "/edit-name", name, PensionSchemeDto.class);
    }
}
