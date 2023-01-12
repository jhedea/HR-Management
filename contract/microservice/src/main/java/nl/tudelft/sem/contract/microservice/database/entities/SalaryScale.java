package nl.tudelft.sem.contract.microservice.database.entities;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.microservice.database.entities.utils.BaseEntity;
import org.modelmapper.ModelMapper;

/**
 * Entity which represents a salary scale.
 */
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
public class SalaryScale extends BaseEntity<SalaryScaleDto> {
    /**
     * Minimum salary for this salary scale.
     */
    @NotBlank
    @Column(nullable = false)
    private BigDecimal minimumPay;

    /**
     * Maximum salary for this salary scale.
     */
    @NotBlank
    @Column(nullable = false)
    private BigDecimal maximumPay;

    @Min(0)
    @Max(1)
    @NotNull
    @NonNull
    private BigDecimal step;

    /**
     * Job positions which have access to this salary scale.
     */
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @OneToMany(mappedBy = "salaryScale")
    private Set<JobPosition> jobPositions;

    /**
     * Create a salary scale from the DTO object.
     *
     * @param salaryScaleDto the salary scale DTO to convert from.
     */
    public SalaryScale(@Valid SalaryScaleDto salaryScaleDto) {
        this.minimumPay = salaryScaleDto.getMinimumPay();
        this.maximumPay = salaryScaleDto.getMaximumPay();
        this.step = salaryScaleDto.getStep();

        if (minimumPay.compareTo(maximumPay) > 0) {
            throw new IllegalArgumentException("Minimum pay cannot be greater than maximum pay");
        }
    }

    @Override
    public SalaryScaleDto getDto() {
        return new ModelMapper().map(this, SalaryScaleDto.class);
    }
}
