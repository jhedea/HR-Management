package nl.tudelft.sem.contract.commons.entities;

import com.fasterxml.jackson.annotation.JsonView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;
import nl.tudelft.sem.contract.commons.entities.utils.Views;
import nl.tudelft.sem.contract.commons.validators.DayOfMonth;

/**
 * Contract modification DTO
 * only contains field that are allowed to be modified.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class ContractModificationDto implements Dto {
    protected ContractStatus status;

    protected ContractType type;

    @Min(8)
    @Max(40)
    protected Integer hoursPerWeek;

    @Min(15)
    @Max(30)
    protected Integer vacationDays;

    @DayOfMonth({1, 15})
    protected LocalDate startDate;

    protected LocalDate endDate;

    protected LocalDate terminationDate;

    @Min(0)
    @Max(1)
    protected BigDecimal salaryScalePoint;

    /**
     * name of job position.
     */
    protected String jobPosition;

    protected List<@NotBlank String> benefits;

    public static ContractModificationDtoBuilder builder() {
        return new ContractModificationDtoBuilder();
    }

    public static class ContractModificationDtoBuilder {
        private transient ContractStatus statusForm;
        private transient ContractType typeForm;
        private transient @Min(8) @Max(40) Integer hoursPerWeekForm;
        private transient @Min(15) @Max(30) Integer vacationDaysForm;
        private transient @DayOfMonth({1, 15}) LocalDate startDateForm;
        private transient LocalDate endDateForm;
        private transient LocalDate terminationDateForm;
        private transient @Min(0) @Max(1) BigDecimal salaryScalePointForm;
        private transient String jobPositionForm;
        private transient List<@NotBlank String> benefitsForm;

        ContractModificationDtoBuilder() {
        }

        public ContractModificationDtoBuilder status(ContractStatus status) {
            this.statusForm = status;
            return this;
        }

        public ContractModificationDtoBuilder type(ContractType type) {
            this.typeForm = type;
            return this;
        }

        public ContractModificationDtoBuilder hoursPerWeek(@Min(8) @Max(40) Integer hoursPerWeek) {
            this.hoursPerWeekForm = hoursPerWeek;
            return this;
        }

        public ContractModificationDtoBuilder vacationDays(@Min(15) @Max(30) Integer vacationDays) {
            this.vacationDaysForm = vacationDays;
            return this;
        }

        public ContractModificationDtoBuilder startDate(@DayOfMonth({1, 15}) LocalDate startDate) {
            this.startDateForm = startDate;
            return this;
        }

        public ContractModificationDtoBuilder endDate(LocalDate endDate) {
            this.endDateForm = endDate;
            return this;
        }

        public ContractModificationDtoBuilder terminationDate(LocalDate terminationDate) {
            this.terminationDateForm = terminationDate;
            return this;
        }

        public ContractModificationDtoBuilder salaryScalePoint(@Min(0) @Max(1) BigDecimal salaryScalePoint) {
            this.salaryScalePointForm = salaryScalePoint;
            return this;
        }

        public ContractModificationDtoBuilder jobPosition(String jobPosition) {
            this.jobPositionForm = jobPosition;
            return this;
        }

        public ContractModificationDtoBuilder benefits(List<@NotBlank String> benefits) {
            this.benefitsForm = benefits;
            return this;
        }

        /**
         * Build the DTO.
         *
         * @return Built DTO.
         */
        public ContractModificationDto build() {
            return new ContractModificationDto(statusForm,
                    typeForm,
                    hoursPerWeekForm,
                    vacationDaysForm,
                    startDateForm,
                    endDateForm,
                    terminationDateForm,
                    salaryScalePointForm,
                    jobPositionForm,
                    benefitsForm);
        }
    }
}
