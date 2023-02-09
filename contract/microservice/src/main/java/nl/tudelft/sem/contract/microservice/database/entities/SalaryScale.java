package nl.tudelft.sem.contract.microservice.database.entities;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.microservice.database.entities.utils.BaseEntity;
import nl.tudelft.sem.contract.microservice.database.entities.utils.Pay;
import org.modelmapper.ModelMapper;

/**
 * Entity which represents a salary scale.
 */
@SuperBuilder

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SalaryScale extends BaseEntity<SalaryScaleDto> {
    /**
     * Minimum and maximum salary for this salary scale.
     */
    @NotNull
    @Embedded
    @Valid
    private Pay pay;

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
        this.pay = new Pay(salaryScaleDto.getMinimumPay(), salaryScaleDto.getMaximumPay());
        this.step = salaryScaleDto.getStep();

        if (pay.getMinimumPay().compareTo(pay.getMaximumPay()) > 0) {
            throw new IllegalArgumentException("Minimum pay cannot be greater than maximum pay");
        }
    }

    /**
     * Generate a SalaryScaleDto from a UUID.
     *
     * @param uuid the UUID to set.
     */
    public SalaryScale(@NonNull UUID uuid) {
        this.setId(uuid);
    }

    @Override
    public SalaryScaleDto getDto() {
        return new ModelMapper().map(this, SalaryScaleDto.class);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pay, step, jobPositions);
    }
}
