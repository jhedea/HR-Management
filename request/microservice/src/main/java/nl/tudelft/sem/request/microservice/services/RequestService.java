package nl.tudelft.sem.request.microservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import lombok.NonNull;
import nl.tudelft.sem.contract.client.ContractClient;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.commons.entities.RequestType;
import nl.tudelft.sem.request.microservice.database.entities.Request;
import nl.tudelft.sem.request.microservice.database.repositories.RequestRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class RequestService {

    private final transient RequestRepository requestRepository;

    private final transient ContractClient contractClient;

    /**
     * Mapper - used to (de)serialize JSON objects.
    */
    private final transient ObjectMapper mapper;

    RequestService(@NonNull RequestRepository requestRepository, ContractClient contractClient) {
        
        this.requestRepository = requestRepository;
        this.contractClient = contractClient;
        mapper = new ObjectMapper();
    }

    /**
     * Reject a request.
     *
     * @param request request to be rejected
     * @param responseBody message that comes along with the rejection
     * @return request with modified attributes
     */
    public Request rejectRequest(Request request, String responseBody) {

        request.setResponseBody(responseBody);
        request.setResponseDate(LocalDateTime.now());
        request.setStatus(RequestStatus.REJECTED);

        requestRepository.save(request);
        return request;
    }

    /**
     * Approve a request.
     *
     * @param request request to be approved
     * @return request with modified attributes
     */
    public Request approveRequest(Request request) {

        if (request.getRequestType() == RequestType.VACATION) {
            CompletableFuture<ContractDto> c = contractClient.contract().getContract(request.getContractId());
            try {
                if (c.get().getVacationDays() < request.getNumberOfDays()) {
                    return rejectRequest(request, "You don't have enough remaining vacation days.");
                } else {
                    contractClient.contract().getContract(request.getContractId())
                            .get().setVacationDays(c.get().getVacationDays() - request.getNumberOfDays());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (request.getRequestType() == RequestType.TERMINATION) {
                contractClient.contract().terminateContract(request.getContractId());
            }

        } else if(request.getRequestType() == RequestType.MODIFY) {
            String[] body = request.getRequestBody().toString().split(",\n");

            try {
                UUID id = UUID.fromString(body[0]);
                ContractModificationDto modDto = mapper.readValue(body[1], ContractModificationDto.class);

                CompletableFuture<ContractDto> c = contractClient.contract().modifyContract(id, modDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.setResponseDate(LocalDateTime.now());
        request.setStatus(RequestStatus.APPROVED);

        requestRepository.save(request);
        return request;
    }

    /**
     *  Stores the modification request in the repository.
     *
     * @param id Id of the draft contract.
     * @param modifications Modifications done to the draft contract.
     * @return The Request Entry
     */
    public Optional<Request> modifyContract(@PathVariable UUID id,
                                            @Valid @RequestBody ContractModificationDto modifications) {
        try {
            Request request = Request.builder()
                    .id(UUID.randomUUID())
                    .status(RequestStatus.OPEN)
                    .author(SecurityContextHolder.getContext().getAuthentication().getName())
                    .requestBody(id.toString() + ",\n" + mapper.writeValueAsString(modifications))
                    .requestDate(LocalDateTime.now())
                    .responseBody(null)
                    .responseDate(null)
                    .startDate(null)
                    .numberOfDays(0)
                    .build();

            requestRepository.save(request);

            return Optional.of(request);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Request> addResponse(UUID id, String response) {
        Optional<Request> request = requestRepository.findById(id);

        if(request.isEmpty()) {
            return request;
        }

        request.get().setResponseBody(response);
        request.get().setStatus(RequestStatus.APPROVED);

        requestRepository.save(request.get());

        return Optional.of(request.get());
    }

    /**
     *  Stores the document request in the repository.
     *
     * @param id The UUID of the employee from which we request the documents.
     * @param body Message of the request.
     * @return The Request Entry.
     */
    public Request addRequestDocument(UUID id, String body) {
        Request request = Request.builder()
                .id(UUID.randomUUID())
                .status(RequestStatus.OPEN)
                .author(SecurityContextHolder.getContext().getAuthentication().getName())
                .requestBody(id.toString() + "\n" + body)
                .requestDate(LocalDateTime.now(ZoneId.of("Europe/Amsterdam")))
                .responseBody(null)
                .responseDate(null)
                .startDate(null)
                .numberOfDays(0)
                .build();

        requestRepository.save(request);

        return request;
    }
}
