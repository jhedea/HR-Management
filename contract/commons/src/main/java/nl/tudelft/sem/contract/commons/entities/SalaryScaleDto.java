package nl.tudelft.sem.contract.commons.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;
import nl.tudelft.sem.contract.commons.entities.utils.Views;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonView(Views.Public.class)
public class SalaryScaleDto implements Dto {
    /**
     * The ID of the salary scale.
     */
    protected UUID id;

    @Min(0)
    @NonNull
    @NotNull
    protected BigDecimal minimumPay;

    @NonNull
    @NotNull
    protected BigDecimal maximumPay;

    @Min(0)
    @Max(1)
    @NonNull
    @NotNull
    protected BigDecimal step;

    protected Set<@NotNull JobPositionDto> jobPositions;

    public static SalaryScaleDtoBuilder builder() {
        return new SalaryScaleDtoBuilder();
    }

    public static class SalaryScaleDtoBuilder {
        private transient UUID idForm;
        private transient @Min(0) @NonNull @NotNull BigDecimal minimumPayForm;
        private transient @NonNull @NotNull BigDecimal maximumPayForm;
        private transient @Min(0) @Max(1) @NonNull @NotNull BigDecimal stepForm;
        private transient Set<@NotNull JobPositionDto> jobPositionsForm;

        public SalaryScaleDtoBuilder() {
        }

        public SalaryScaleDtoBuilder id(UUID id) {
            this.idForm = id;
            return this;
        }

        public SalaryScaleDtoBuilder minimumPay(@Min(0) @NonNull @NotNull BigDecimal minimumPay) {
            this.minimumPayForm = minimumPay;
            return this;
        }

        public SalaryScaleDtoBuilder maximumPay(@NonNull @NotNull BigDecimal maximumPay) {
            this.maximumPayForm = maximumPay;
            return this;
        }

        public SalaryScaleDtoBuilder step(@Min(0) @Max(1) @NonNull @NotNull BigDecimal step) {
            this.stepForm = step;
            return this;
        }

        public SalaryScaleDtoBuilder jobPositions(Set<@NotNull JobPositionDto> jobPositions) {
            this.jobPositionsForm = jobPositions;
            return this;
        }

        public SalaryScaleDto build() {
            return new SalaryScaleDto(idForm, minimumPayForm, maximumPayForm, stepForm, jobPositionsForm);
        }
    }
}
