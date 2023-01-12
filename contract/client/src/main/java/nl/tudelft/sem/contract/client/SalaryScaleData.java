package nl.tudelft.sem.contract.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;

@RequiredArgsConstructor
public class SalaryScaleData {
    private static String startPath = "/salary-scale/";

    @NonNull
    private final transient ContractClient client;

    public CompletableFuture<SalaryScaleDto> getSalaryScale(@NonNull UUID salaryScaleId) {
        return client.get(startPath + salaryScaleId, SalaryScaleDto.class);
    }

    public CompletableFuture<SalaryScaleDto> addSalaryScale(@NonNull SalaryScaleDto salaryScale)
            throws JsonProcessingException {
        return client.post("/salary-scale", salaryScale, SalaryScaleDto.class);
    }

    public CompletableFuture<SalaryScaleDto> removeSalaryScale(@NonNull UUID salaryScaleId) {
        return client.delete(startPath + salaryScaleId, SalaryScaleDto.class);
    }

    public CompletableFuture<SalaryScaleDto> editMinimumPay(@NonNull UUID salaryScaleId, @NonNull StringDto minimumPay)
            throws JsonProcessingException {
        return client.put(startPath + salaryScaleId + "/edit-minimum-pay", minimumPay, SalaryScaleDto.class);
    }

    public CompletableFuture<SalaryScaleDto> editMaximumPay(@NonNull UUID salaryScaleId, @NonNull StringDto maximumPay)
            throws JsonProcessingException {
        return client.put(startPath + salaryScaleId + "/edit-maximum-pay", maximumPay, SalaryScaleDto.class);
    }

}
