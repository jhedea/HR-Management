package nl.tudelft.sem.contract.microservice.database.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractType;
import nl.tudelft.sem.contract.commons.validators.DayOfMonth;
import nl.tudelft.sem.contract.microservice.database.entities.utils.BaseEntity;
import org.modelmapper.ModelMapper;

@SuppressWarnings("com.haulmont.jpb.LombokEqualsAndHashCodeInspection")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract extends BaseEntity<ContractDto> {
    /**
     * ID of the employee with the contract.
     */
    @NonNull
    @NotNull
    @Column(updatable = false, nullable = false)
    private UUID employeeId;

    /**
     * ID of the employer with the contract.
     */
    @NonNull
    @NotNull
    @Column(updatable = false, nullable = false)
    private UUID employerId;

    /**
     * Type of contract.
     */
    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType type;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

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
     * The position of the employee.
     */
    @NonNull
    @NotNull
    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobPosition jobPosition;

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

    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "pension_scheme_id", nullable = false)
    private PensionScheme pensionScheme;

    /**
     * The benefits the employee is entitled to.
     */
    @NonNull
    @NotNull
    @ElementCollection
    @Column(name = "benefit")
    @CollectionTable(name = "contract_benefits", joinColumns = @JoinColumn(name = "owner_id"))
    private List<@NotBlank String> benefits;

    /**
     * Create a new contract entity.
     *
     *  @param dto the dto to convert
     * @param jobPosition the job position to link to
     */
    public Contract(@Valid ContractDto dto, JobPosition jobPosition, PensionScheme pensionScheme) {
        this(dto.getEmployeeId(),
                dto.getEmployerId(),
                dto.getType(),
                dto.getStatus(),
                dto.getHoursPerWeek(),
                dto.getVacationDays(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getTerminationDate(),
                jobPosition,
                dto.getSalaryScalePoint(),
                Objects.requireNonNullElse(dto.getLastSalaryIncreaseDate(), dto.getStartDate()),
                pensionScheme,
                dto.getBenefits());
    }

    @Override
    public ContractDto getDto() {
        return new ModelMapper().map(this, ContractDto.class);
    }

    public boolean isDraft() {
        return this.status == ContractStatus.DRAFT;
    }

    public boolean isActive() {
        return this.status == ContractStatus.ACTIVE;
    }
}
