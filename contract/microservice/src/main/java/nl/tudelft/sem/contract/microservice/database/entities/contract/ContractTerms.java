package nl.tudelft.sem.contract.microservice.database.entities.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractTermsDto;
import nl.tudelft.sem.contract.commons.validators.DayOfMonth;

@Embeddable
@NoArgsConstructor
public class ContractTerms {
    /**
     * The amount of hours the employee is expected to work per week.
     */
    @Column(nullable = false)
    @Min(8)
    @Max(40)
    private int hoursPerWeek;

    /**
     * Number of days of vacation the employee is entitled to per year.
     */
    @Column(nullable = false)
    @Min(15)
    @Max(30)
    private int vacationDays;

    /**
     * The date of the start of the contract.
     */
    @DayOfMonth({1, 15})
    @NotNull
    private LocalDate startDate;

    /**
     * The date when the contract is scheduled to end. Can be null.
     */
    private LocalDate endDate;

    /**
     * The date on which the contract was terminated.
     */
    protected LocalDate terminationDate;

    /**
     * The salary scale point of the employee.
     * 0 will yield the minimum salary of the position (thus salary scale)
     * 1 will yield the maximum salary of the position (thus salary scale)
     */
    @NotNull
    @Min(0)
    @Max(1)
    private BigDecimal salaryScalePoint;

    /**
     * The date on which the salary was automatically increased.
     */
    @NotNull
    @NonNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDate lastSalaryIncreaseDate;

    /**
     * Construct a new ContractTerms class from the DTO.
     *
     * @param dto the dto to convert
     */
    public ContractTerms(ContractTermsDto dto) {
        this.hoursPerWeek = dto.getHoursPerWeek();
        this.vacationDays = dto.getVacationDays();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.terminationDate = dto.getTerminationDate();
        this.salaryScalePoint = dto.getSalaryScalePoint();
        this.lastSalaryIncreaseDate = Objects.requireNonNullElse(dto.getLastSalaryIncreaseDate(), dto.getStartDate());
    }

    /**
     * Create a new Terms object.
     *
     * @param hoursPerWeek the hoursPerWeek to set
     * @param vacationDays the vacationDays to set
     * @param startDate   the startDate to set
     * @param endDate    the endDate to set
     * @param terminationDate the terminationDate to set
     * @param salaryScalePoint the salaryScalePoint to set
     * @param lastSalaryIncreaseDate the lastSalaryIncreaseDate to set
     */
    public ContractTerms(@Min(8) @Max(40) int hoursPerWeek,
                         @Min(15) @Max(30) int vacationDays,
                         @DayOfMonth({1, 15}) @NotNull LocalDate startDate,
                         LocalDate endDate,
                         LocalDate terminationDate,
                         @NotNull @Min(0) @Max(1) BigDecimal salaryScalePoint,
                         @PastOrPresent LocalDate lastSalaryIncreaseDate) {
        this.hoursPerWeek = hoursPerWeek;
        this.vacationDays = vacationDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.terminationDate = terminationDate;
        this.salaryScalePoint = salaryScalePoint;
        this.lastSalaryIncreaseDate = Objects.requireNonNullElse(lastSalaryIncreaseDate, startDate);
    }

    /**
     * Modifies the contract terms.
     *
     * @param modify set of modifications
     */
    public void modify(ContractModificationDto modify) {
        if (modify.getHoursPerWeek() != null) {
            setHoursPerWeek(modify.getHoursPerWeek());
        }
        if (modify.getVacationDays() != null) {
            setVacationDays(modify.getVacationDays());
        }
        setStartDate(modify.getStartDate());
        setEndDate(modify.getEndDate());
        setSalaryScalePoint(modify.getSalaryScalePoint());
    }

    public static ContractTermsBuilder builder() {
        return new ContractTermsBuilder();
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    /**
     * Hours per week setter, checks validity of input before setting value.
     *
     * @param hoursPerWeek new value
     */
    public void setHoursPerWeek(int hoursPerWeek) {
        if (hoursPerWeek >= 8 && hoursPerWeek <= 40) {
            this.hoursPerWeek = hoursPerWeek;
        }
    }

    public int getVacationDays() {
        return vacationDays;
    }

    /**
     * Vacation days setter, checks validity of input before setting.
     *
     * @param vacationDays new days
     */
    public void setVacationDays(int vacationDays) {
        if (vacationDays >= 15 && vacationDays <= 30) {
            this.vacationDays = vacationDays;
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * New start date checked for validity before setting.
     *
     * @param startDate new start date
     */
    public void setStartDate(LocalDate startDate) {
        if (startDate != null && (startDate.getDayOfMonth() == 1 || startDate.getDayOfMonth() == 15)) {
            this.startDate = startDate;
        }
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * New end date checked for validity before setting.
     *
     * @param endDate new end date
     */
    public void setEndDate(LocalDate endDate) {
        if (endDate != null) {
            this.endDate = endDate;
        }
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public BigDecimal getSalaryScalePoint() {
        return salaryScalePoint;
    }

    /**
     * new salary scale point checked for validity before setting.
     *
     * @param salaryScalePoint new salary scale point
     */
    public void setSalaryScalePoint(BigDecimal salaryScalePoint) {
        if (salaryScalePoint != null && salaryScalePoint.compareTo(BigDecimal.ZERO) >= 0
            && salaryScalePoint.compareTo(BigDecimal.ONE) <= 0) {
            this.salaryScalePoint = salaryScalePoint;
        }
    }

    public @NonNull LocalDate getLastSalaryIncreaseDate() {
        return lastSalaryIncreaseDate;
    }

    public void setLastSalaryIncreaseDate(@NonNull LocalDate lastSalaryIncreaseDate) {
        this.lastSalaryIncreaseDate = lastSalaryIncreaseDate;
    }
}
