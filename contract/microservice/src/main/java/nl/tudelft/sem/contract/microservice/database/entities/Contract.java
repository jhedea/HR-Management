package nl.tudelft.sem.contract.microservice.database.entities;

import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractInfo;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractParties;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractTerms;
import nl.tudelft.sem.contract.microservice.database.entities.utils.BaseEntity;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract extends BaseEntity<ContractDto> {
    @Embedded
    @NonNull
    private ContractParties contractParties;

    @Embedded
    @NonNull
    private ContractInfo contractInfo;

    @Embedded
    @NonNull
    private ContractTerms contractTerms;

    /**
     * The position of the employee.
     */
    @NonNull
    @NotNull
    @ManyToOne(cascade = javax.persistence.CascadeType.ALL, optional = false)
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobPosition jobPosition;

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL, optional = false)
    @JoinColumn(name = "pension_scheme_id", nullable = false)
    private PensionScheme pensionScheme;

    /**
     * The benefits the employee is entitled to.
     */
    @NonNull
    @NotNull
    @ElementCollection
    @Column(name = "benefit")
    @CollectionTable(name = "contract_benefits", joinColumns = @JoinColumn(name = "owner_id"))
    private List<@NotBlank String> benefits;

    /**
     * Create a new contract entity.
     *
     *  @param dto the dto to convert
     * @param jobPosition the job position to link to
     */
    public Contract(@Valid ContractDto dto, JobPosition jobPosition, PensionScheme pensionScheme) {
        this(new ContractParties(dto.getContractParties()),
                new ContractInfo(dto.getContractInfo()),
                new ContractTerms(dto.getContractTerms()),
                jobPosition,
                pensionScheme,
                dto.getBenefits());
    }

    @Override
    public ContractDto getDto() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return new ModelMapper().map(this, ContractDto.class);
    }

    public boolean isDraft() {
        return this.contractInfo.getStatus() == ContractStatus.DRAFT;
    }

    public boolean isActive() {
        return this.contractInfo.getStatus() == ContractStatus.ACTIVE;
    }

    /**
     * Modifies a draft contract with given input. Contract needs to be in draft stage,
     * and any of the input values can be null, this need to be checked for validity before modifying.
     *
     * @param modify to be modified values
     * @param newJob possible to be modified job position
     * @throws ActionNotAllowedException when the contract does not have the draft status
     */
    public void modifyDraft(ContractModificationDto modify, JobPosition newJob) throws ActionNotAllowedException {
        if (isDraft()) {
            contractInfo.modifyType(modify.getType());
            contractInfo.modifyStatus(modify.getStatus());
            contractTerms.modify(modify);
            setJobPosition(newJob);
            setBenefits(modify.getBenefits());
        } else {
            throw new ActionNotAllowedException("Contract is not in draft, thus cannot be modified.");
        }
    }

    public void setContractParties(@NonNull ContractParties contractParties) {
        this.contractParties = contractParties;
    }

    public void setContractInfo(@NonNull ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    public void setContractTerms(@NonNull ContractTerms contractTerms) {
        this.contractTerms = contractTerms;
    }

    /**
     * Validation check before new job position is set.
     *
     * @param jobPosition new job position
     */
    public void setJobPosition(JobPosition jobPosition) {
        if (jobPosition != null) {
            this.jobPosition = jobPosition;
        }
    }

    /**
     * Validation check before new benefits are set.
     *
     * @param benefits new benefits
     */
    public void setBenefits(List<@NotBlank String> benefits) {
        if (benefits != null) {
            this.benefits = benefits;
        }
    }
}
