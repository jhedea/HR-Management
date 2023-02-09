package nl.tudelft.sem.request.microservice.database.entities;

import javax.persistence.Entity;
import nl.tudelft.sem.contract.client.ContractClient;
import nl.tudelft.sem.request.microservice.services.RequestService;

@Entity
public class TerminationRequest extends GeneralRequest {
    /**
     * Approve a request.
     *
     * @param contractClient the Contract Client
     * @param requestService the Request Service
     */
    public void approveRequest(ContractClient contractClient, RequestService requestService) {
        contractClient.contract().terminateContract(this.getContractId());
    }
}
