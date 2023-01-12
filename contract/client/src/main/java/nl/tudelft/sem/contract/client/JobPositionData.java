package nl.tudelft.sem.contract.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.contract.commons.entities.JobPositionDto;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;

@RequiredArgsConstructor
public class JobPositionData {
    private static final String path = "/job-position/";

    @NonNull
    private final transient ContractClient client;

    public CompletableFuture<JobPositionDto> getJobPosition(@NonNull UUID jobPositionId) {
        return client.get(path + jobPositionId, JobPositionDto.class);
    }

    public CompletableFuture<JobPositionDto> addJobPosition(@NonNull JobPositionDto jobPositionDto)
            throws JsonProcessingException {
        return client.post("/job-position", jobPositionDto, JobPositionDto.class);
    }

    public CompletableFuture<JobPositionDto> deleteJobPosition(@NonNull UUID jobPositionId) {
        return client.delete(path + jobPositionId, JobPositionDto.class);
    }

    public CompletableFuture<JobPositionDto> editName(@NonNull UUID jobPositionId, @NonNull StringDto name)
            throws JsonProcessingException {
        return client.put(path + jobPositionId + "/edit-name", name, JobPositionDto.class);
    }

    public CompletableFuture<JobPositionDto> editSalaryScale(@NonNull UUID jobPositionId,
                                                             @NonNull SalaryScaleDto salaryScale)
            throws JsonProcessingException {
        return client.put(path + jobPositionId + "/edit-salary-scale", salaryScale, JobPositionDto.class);
    }

}
