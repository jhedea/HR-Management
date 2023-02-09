package nl.tudelft.sem.contract.microservice.database.entities.contract;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.contract.commons.entities.ContractPartiesDto;
import org.hibernate.annotations.Type;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContractParties {
    /**
     * ID of the employee with the contract.
     */
    @NonNull
    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(updatable = false, nullable = false)
    private UUID employeeId;

    /**
     * ID of the employer with the contract.
     */
    @NonNull
    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(updatable = false, nullable = false)
    private UUID employerId;

    public ContractParties(ContractPartiesDto dto) {
        this(dto.getEmployeeId(), dto.getEmployerId());
    }

    public @NonNull UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@NonNull UUID employeeId) {
        this.employeeId = employeeId;
    }

    public @NonNull UUID getEmployerId() {
        return employerId;
    }

    public void setEmployerId(@NonNull UUID employerId) {
        this.employerId = employerId;
    }
}
