package nl.tudelft.sem.contract.commons.entities;

import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractPartiesDto {
    @NonNull
    protected UUID employeeId;

    @NonNull
    protected UUID employerId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractPartiesDto that = (ContractPartiesDto) o;
        return this == o || (employeeId.equals(that.employeeId) && employerId.equals(that.employerId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, employerId);
    }
}
