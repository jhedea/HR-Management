package nl.tudelft.sem.contract.microservice.database.entities.contract;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.contract.commons.entities.ContractInfoDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractType;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContractInfo {
    /**
     * Type of contract.
     */
    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType type;

    /**
     * Current status of the contract.
     */
    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    public ContractInfo(ContractInfoDto dto) {
        this(dto.getType(), dto.getStatus());
    }

    public @NonNull ContractType getType() {
        return type;
    }

    public void setType(@NonNull ContractType type) {
        this.type = type;
    }

    public @NonNull ContractStatus getStatus() {
        return status;
    }

    public void setStatus(@NonNull ContractStatus status) {
        this.status = status;
    }

    /**
     * Modifies the contract type if it is not null.
     *
     * @param newType new contract type
     */
    public void modifyType(ContractType newType) {
        if (newType != null) {
            this.type = newType;
        }
    }

    /**
     * Modifies the contract status if it is not null.
     *
     * @param newStatus new contract status
     */
    public void modifyStatus(ContractStatus newStatus) {
        if (newStatus != null) {
            this.status = newStatus;
        }
    }
}
