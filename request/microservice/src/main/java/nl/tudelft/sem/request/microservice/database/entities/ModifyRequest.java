package nl.tudelft.sem.request.microservice.database.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.client.ContractClient;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.request.microservice.services.RequestService;

@Entity
@Slf4j
public class ModifyRequest extends GeneralRequest {
    /**
     * Approve a request.
     *
     * @param contractClient the Contract Client
     * @param requestService the Request Service
     */
    public void approveRequest(ContractClient contractClient, RequestService requestService) {
        String[] body = this.getRequestBody().split(",\n");

        try {
            UUID id = UUID.fromString(body[0]);
            ContractModificationDto modDto = new ObjectMapper().readValue(body[1], ContractModificationDto.class);
            contractClient.contract().modifyContract(id, modDto).join();
        } catch (Exception e) {
            log.error("Error while approving request: ", e);
        }
    }
}
