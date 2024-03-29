package nl.tudelft.sem.contract.commons.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;
import nl.tudelft.sem.contract.commons.entities.utils.Views;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonView(Views.Public.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class JobPositionDto implements Dto {
    /**
     * The ID of the job position.
     */
    protected UUID id;

    /**
     * The name of the job position.
     */
    @NotBlank
    protected String name;

    /**
     * The contracts associated with this job position.
     */
    protected Set<@NotNull ContractDto> contracts;

    /**
     * The salary scale associated with this job position.
     */
    @NotNull
    protected SalaryScaleDto salaryScale;

    public static JobPositionDtoBuilder builder() {
        return new JobPositionDtoBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobPositionDto that = (JobPositionDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(contracts, that.contracts)
                && Objects.equals(salaryScale, that.salaryScale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contracts, salaryScale);
    }

    public static class JobPositionDtoBuilder {
        private transient UUID idForm;
        private transient @NotBlank String nameForm;
        private transient Set<@NotNull ContractDto> contractsForm;
        private transient @NotNull SalaryScaleDto salaryScaleForm;

        JobPositionDtoBuilder() {
        }

        public JobPositionDtoBuilder id(UUID id) {
            this.idForm = id;
            return this;
        }

        public JobPositionDtoBuilder name(@NotBlank String name) {
            this.nameForm = name;
            return this;
        }

        public JobPositionDtoBuilder contracts(Set<@NotNull ContractDto> contracts) {
            this.contractsForm = contracts;
            return this;
        }

        public JobPositionDtoBuilder salaryScale(@NotNull SalaryScaleDto salaryScale) {
            this.salaryScaleForm = salaryScale;
            return this;
        }

        public JobPositionDto build() {
            return new JobPositionDto(idForm, nameForm, contractsForm, salaryScaleForm);
        }
    }
}
