package nl.tudelft.sem.contract.commons.entities;

import com.fasterxml.jackson.annotation.JsonView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;
import nl.tudelft.sem.contract.commons.entities.utils.Views;
import nl.tudelft.sem.contract.commons.validators.DayOfMonth;

/**
 * Contract data transfer object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class ContractDto implements Dto {
    /**
     * The ID of the contract.
     */
    protected UUID id;

    protected UUID employeeId;

    protected UUID employerId;

    protected ContractType type;

    protected ContractStatus status;

    @Min(8)
    @Max(40)
    protected int hoursPerWeek;

    @Min(15)
    @Max(30)
    protected int vacationDays;

    @DayOfMonth({1, 15})
    protected LocalDate startDate;

    protected LocalDate endDate;

    protected LocalDate terminationDate;

    @Min(0)
    @Max(1)
    @NotNull
    @NonNull
    protected BigDecimal salaryScalePoint = BigDecimal.ZERO;

    @PastOrPresent
    private LocalDate lastSalaryIncreaseDate;

    protected JobPositionDto jobPosition;

    protected PensionSchemeDto pensionScheme;

    protected List<@NotBlank String> benefits;

    public static ContractDtoBuilder builder() {
        return new ContractDtoBuilder();
    }

    public static class ContractDtoBuilder {
        private transient UUID idForm;
        private transient UUID employeeIdForm;
        private transient UUID employerIdForm;
        private transient ContractType typeForm;
        private transient ContractStatus statusForm;
        private transient @Min(8) @Max(40) int hoursPerWeekForm;
        private transient @Min(15) @Max(30) int vacationDaysForm;
        private transient @DayOfMonth({1, 15}) LocalDate startDateForm;
        private transient LocalDate endDateForm;
        private transient LocalDate terminationDateForm;
        private transient @Min(0) @Max(1) @NotNull @NonNull BigDecimal salaryScalePointForm;
        private transient @PastOrPresent LocalDate lastSalaryIncreaseDateForm;
        private transient JobPositionDto jobPositionForm;
        private transient PensionSchemeDto pensionSchemeForm;
        private transient List<@NotBlank String> benefitsForm;

        ContractDtoBuilder() {
        }

        public ContractDtoBuilder id(UUID id) {
            this.idForm = id;
            return this;
        }

        public ContractDtoBuilder employeeId(UUID employeeId) {
            this.employeeIdForm = employeeId;
            return this;
        }

        public ContractDtoBuilder employerId(UUID employerId) {
            this.employerIdForm = employerId;
            return this;
        }

        public ContractDtoBuilder type(ContractType type) {
            this.typeForm = type;
            return this;
        }

        public ContractDtoBuilder status(ContractStatus status) {
            this.statusForm = status;
            return this;
        }

        public ContractDtoBuilder hoursPerWeek(@Min(8) @Max(40) int hoursPerWeek) {
            this.hoursPerWeekForm = hoursPerWeek;
            return this;
        }

        public ContractDtoBuilder vacationDays(@Min(15) @Max(30) int vacationDays) {
            this.vacationDaysForm = vacationDays;
            return this;
        }

        public ContractDtoBuilder startDate(@DayOfMonth({1, 15}) LocalDate startDate) {
            this.startDateForm = startDate;
            return this;
        }

        public ContractDtoBuilder endDate(LocalDate endDate) {
            this.endDateForm = endDate;
            return this;
        }

        public ContractDtoBuilder terminationDate(LocalDate terminationDate) {
            this.terminationDateForm = terminationDate;
            return this;
        }

        public ContractDtoBuilder salaryScalePoint(@Min(0) @Max(1) @NotNull @NonNull BigDecimal salaryScalePoint) {
            this.salaryScalePointForm = salaryScalePoint;
            return this;
        }

        public ContractDtoBuilder lastSalaryIncreaseDate(@PastOrPresent LocalDate lastSalaryIncreaseDate) {
            this.lastSalaryIncreaseDateForm = lastSalaryIncreaseDate;
            return this;
        }

        public ContractDtoBuilder jobPosition(JobPositionDto jobPosition) {
            this.jobPositionForm = jobPosition;
            return this;
        }

        public ContractDtoBuilder pensionScheme(PensionSchemeDto pensionScheme) {
            this.pensionSchemeForm = pensionScheme;
            return this;
        }

        public ContractDtoBuilder benefits(List<@NotBlank String> benefits) {
            this.benefitsForm = benefits;
            return this;
        }

        /**
         * Build the DTO.
         *
         * @return Built DTO.
         */
        public ContractDto build() {
            return new ContractDto(idForm,
                    employeeIdForm,
                    employerIdForm,
                    typeForm,
                    statusForm,
                    hoursPerWeekForm,
                    vacationDaysForm,
                    startDateForm,
                    endDateForm,
                    terminationDateForm,
                    salaryScalePointForm,
                    lastSalaryIncreaseDateForm,
                    jobPositionForm,
                    pensionSchemeForm,
                    benefitsForm);
        }
    }
}
