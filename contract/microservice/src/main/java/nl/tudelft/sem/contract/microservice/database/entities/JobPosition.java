package nl.tudelft.sem.contract.microservice.database.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.contract.commons.entities.JobPositionDto;
import nl.tudelft.sem.contract.microservice.database.entities.utils.BaseEntity;
import org.modelmapper.ModelMapper;

@SuperBuilder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobPosition extends BaseEntity<JobPositionDto> {
    @NonNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "jobPosition", orphanRemoval = true)
    @ToString.Exclude
    private Set<Contract> contracts;

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL, optional = false)
    @JoinColumn(name = "scale_scale_id", nullable = false)
    @NotNull
    private SalaryScale salaryScale;

    public JobPosition(JobPositionDto jobPositionDto) {
        this.name = jobPositionDto.getName();
        this.salaryScale = new SalaryScale(jobPositionDto.getSalaryScale());
    }

    @Override
    public JobPositionDto getDto() {
        return new ModelMapper().map(this, JobPositionDto.class);
    }
}
