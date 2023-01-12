package nl.tudelft.sem.contract.commons.entities;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;
import nl.tudelft.sem.contract.commons.entities.utils.Views;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
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
