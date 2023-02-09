package nl.tudelft.sem.contract.commons.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import nl.tudelft.sem.contract.commons.validators.DayOfMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractTermsDto {
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

    public static ContractTermsBuilder builder() {
        return new ContractTermsBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractTermsDto that = (ContractTermsDto) o;
        return getHoursPerWeek() == that.getHoursPerWeek() && getVacationDays() == that.getVacationDays()
                && Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getEndDate(), that.getEndDate())
                && Objects.equals(getTerminationDate(), that.getTerminationDate())
                && getSalaryScalePoint().equals(that.getSalaryScalePoint())
                && Objects.equals(getLastSalaryIncreaseDate(), that.getLastSalaryIncreaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHoursPerWeek(), getVacationDays(), getStartDate(),
                getEndDate(), getTerminationDate(), getSalaryScalePoint(), getLastSalaryIncreaseDate());
    }

    public static class ContractTermsBuilder {
        private transient @Min(8) @Max(40) int hoursPerWeekForm;
        private transient @Min(15) @Max(30) int vacationDaysForm;
        private transient @DayOfMonth({1, 15}) LocalDate startDateForm;
        private transient LocalDate endDateForm;
        private transient LocalDate terminationDateForm;
        private transient @Min(0) @Max(1) @NotNull @NonNull BigDecimal salaryScalePointForm;
        private transient @PastOrPresent LocalDate lastSalaryIncreaseDateForm;

        ContractTermsBuilder() {
        }

        public ContractTermsBuilder hoursPerWeek(@Min(8) @Max(40) int hoursPerWeek) {
            this.hoursPerWeekForm = hoursPerWeek;
            return this;
        }

        public ContractTermsBuilder vacationDays(@Min(15) @Max(30) int vacationDays) {
            this.vacationDaysForm = vacationDays;
            return this;
        }

        public ContractTermsBuilder startDate(@DayOfMonth({1, 15}) LocalDate startDate) {
            this.startDateForm = startDate;
            return this;
        }

        public ContractTermsBuilder endDate(LocalDate endDate) {
            this.endDateForm = endDate;
            return this;
        }

        public ContractTermsBuilder terminationDate(LocalDate terminationDate) {
            this.terminationDateForm = terminationDate;
            return this;
        }

        public ContractTermsBuilder salaryScalePoint(@Min(0) @Max(1) @NotNull @NonNull BigDecimal salaryScalePoint) {
            this.salaryScalePointForm = salaryScalePoint;
            return this;
        }

        public ContractTermsBuilder lastSalaryIncreaseDate(@PastOrPresent LocalDate lastSalaryIncreaseDate) {
            this.lastSalaryIncreaseDateForm = lastSalaryIncreaseDate;
            return this;
        }

        public ContractTermsDto build() {
            return new ContractTermsDto(hoursPerWeekForm, vacationDaysForm, startDateForm, endDateForm,
                    terminationDateForm, salaryScalePointForm, lastSalaryIncreaseDateForm);
        }

    }
}
