package nl.tudelft.sem.request.microservice.database.entities;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.client.ContractClient;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.microservice.services.RequestService;


@Entity
@SuperBuilder
@NoArgsConstructor
@Slf4j
public class VacationRequest extends GeneralRequest {


    /**
     * Approve a request.
     *
     * @param contractClient the Contract Client
     * @param requestService the Request Service
     */
    public void approveRequest(ContractClient contractClient, RequestService requestService) {
        CompletableFuture<ContractDto> c = contractClient.contract().getContract(this.getContractId());
        try {
            if (c.get().getContractTerms().getVacationDays() < this.getNumberOfDays()) {
                this.rejectRequest("You don't have enough remaining vacation days.");
            } else {
                contractClient.contract().getContract(this.getContractId())
                        .get().getContractTerms().setVacationDays(
                                c.get().getContractTerms().getVacationDays() - this.getNumberOfDays());
                this.setResponseDate(LocalDateTime.now());
                this.setStatus(RequestStatus.APPROVED);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while approving request: ", e);
            throw new RuntimeException(e);
        }
    }
}
