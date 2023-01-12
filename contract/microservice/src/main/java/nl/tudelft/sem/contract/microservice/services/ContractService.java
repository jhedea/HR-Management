package nl.tudelft.sem.contract.microservice.services;

import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.entities.PensionScheme;
import nl.tudelft.sem.contract.microservice.database.repositories.ContractRepository;
import nl.tudelft.sem.contract.microservice.database.repositories.JobPositionRepository;
import nl.tudelft.sem.contract.microservice.database.repositories.PensionSchemeRepository;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import nl.tudelft.sem.contract.microservice.exceptions.ContractNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    private final transient ContractRepository contractRepository;

    private final transient JobPositionRepository jobPositionRepository;

    private final transient PensionSchemeRepository pensionSchemeRepository;

    ContractService(ContractRepository contractRepository, JobPositionRepository jobPositionRepository,
                    PensionSchemeRepository pensionSchemeRepository) {
        this.contractRepository = contractRepository;
        this.jobPositionRepository = jobPositionRepository;
        this.pensionSchemeRepository = pensionSchemeRepository;
    }

    /**
     * Add new contract to contract repository.
     *
     * @param dto dto of contract to be added
     * @return the contract that was added
     */
    public Contract addContract(ContractDto dto) {
        dto.setStatus(ContractStatus.DRAFT);
        JobPosition jobPosition = jobPositionRepository.save(new JobPosition(dto.getJobPosition()));
        PensionScheme pensionScheme = pensionSchemeRepository.save(new PensionScheme(dto.getPensionScheme()));
        Contract contract = contractRepository.save(new Contract(dto, jobPosition, pensionScheme));
        return contract;
    }

    /**
     * Mark a contract as terminated.
     *
     * @param contractId the id of the contract to terminate.
     */
    public void terminateContract(UUID contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new ActionNotAllowedException("Only active contracts can be terminated.");
        }
        contract.setStatus(ContractStatus.TERMINATED);
        contractRepository.save(contract);
    }

    /**
     * Modify a contract that is in the draft.
     *
     * @param contract contract to be modified
     * @param modify the modifications to be implemented
     * @return the modified contract
     */
    public Contract modifyDraftContract(Contract contract, @Valid ContractModificationDto modify) {
        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new ActionNotAllowedException("Contract is not in draft phase");
        }

        if (modify.getType() != null) {
            contract.setType(modify.getType());
        }
        if (modify.getHoursPerWeek() != null) {
            contract.setHoursPerWeek(modify.getHoursPerWeek());
        }
        if (modify.getVacationDays() != null) {
            contract.setVacationDays(modify.getVacationDays());
        }
        if (modify.getStartDate() != null) {
            contract.setStartDate(modify.getStartDate());
        }
        if (modify.getEndDate() != null) {
            contract.setEndDate(modify.getEndDate());
        }
        if (modify.getSalaryScalePoint() != null) {
            contract.setSalaryScalePoint(modify.getSalaryScalePoint());
        }
        if (modify.getJobPosition() != null) {
            Optional<JobPosition> job = jobPositionRepository.findByName(modify.getJobPosition());
            job.ifPresent(contract::setJobPosition);
        }
        if (modify.getBenefits() != null) {
            contract.setBenefits(modify.getBenefits());
        }

        contractRepository.save(contract);
        return contract;
    }
}
