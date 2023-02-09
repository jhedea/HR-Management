package nl.tudelft.sem.contract.microservice.database.entities.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.NonNull;
import nl.tudelft.sem.contract.commons.validators.DayOfMonth;

public class ContractTermsBuilder {
    private transient @Min(8) @Max(40) int hoursPerWeekField;
    private transient @Min(15) @Max(30) int vacationDaysField;
    private transient @DayOfMonth({1, 15}) @NotNull LocalDate startDateField;
    private transient LocalDate endDateField;
    private transient LocalDate terminationDateField;
    private transient @NotNull @Min(0) @Max(1) BigDecimal salaryScalePointField;
    private transient @NotNull @PastOrPresent LocalDate lastSalaryIncreaseDateField;

    public ContractTermsBuilder hoursPerWeek(@Min(8) @Max(40) int hoursPerWeek) {
        this.hoursPerWeekField = hoursPerWeek;
        return this;
    }

    public ContractTermsBuilder vacationDays(@Min(15) @Max(30) int vacationDays) {
        this.vacationDaysField = vacationDays;
        return this;
    }

    public ContractTermsBuilder startDate(@DayOfMonth({1, 15}) @NotNull @NonNull LocalDate startDate) {
        this.startDateField = startDate;
        return this;
    }

    public ContractTermsBuilder endDate(LocalDate endDate) {
        this.endDateField = endDate;
        return this;
    }

    public ContractTermsBuilder terminationDate(LocalDate terminationDate) {
        this.terminationDateField = terminationDate;
        return this;
    }

    public ContractTermsBuilder salaryScalePoint(@NotNull @Min(0) @Max(1) BigDecimal salaryScalePoint) {
        this.salaryScalePointField = salaryScalePoint;
        return this;
    }

    public ContractTermsBuilder lastSalaryIncreaseDate(@PastOrPresent LocalDate lastSalaryIncreaseDate) {
        this.lastSalaryIncreaseDateField = lastSalaryIncreaseDate;
        return this;
    }

    /**
     * Build a new ContractTerms object.
     *
     * @return built ContractTerms object.
     */
    public ContractTerms build() {
        return new ContractTerms(hoursPerWeekField,
                vacationDaysField,
                startDateField,
                endDateField,
                terminationDateField,
                salaryScalePointField,
                lastSalaryIncreaseDateField);
    }
}