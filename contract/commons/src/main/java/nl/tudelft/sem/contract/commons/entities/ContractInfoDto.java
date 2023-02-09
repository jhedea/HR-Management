package nl.tudelft.sem.contract.commons.entities;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractInfoDto {
    @NonNull
    protected ContractType type;

    @NonNull
    protected ContractStatus status;

    public static ContractInfoBuilder builder() {
        return new ContractInfoBuilder();
    }


    public static class ContractInfoBuilder {
        private transient ContractType typeForm;
        private transient ContractStatus statusForm;

        ContractInfoBuilder() {
        }

        public ContractInfoBuilder type(ContractType type) {
            this.typeForm = type;
            return this;
        }

        public ContractInfoBuilder status(ContractStatus status) {
            this.statusForm = status;
            return this;
        }

        public ContractInfoDto build() {
            return new ContractInfoDto(typeForm, statusForm);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractInfoDto that = (ContractInfoDto) o;
        return this == o || (type == that.type && status == that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status);
    }
}
