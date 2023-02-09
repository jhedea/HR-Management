package nl.tudelft.sem.contract.microservice.database.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.contract.commons.entities.PensionSchemeDto;
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
public class PensionScheme extends BaseEntity<PensionSchemeDto> {
    /**
     * The name of the pension scheme.
     */
    @NonNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "pensionScheme")
    @ToString.Exclude
    private Set<Contract> contracts;

    public PensionScheme(PensionSchemeDto pensionSchemeDto) {
        this.setId(pensionSchemeDto.getId());
        this.name = pensionSchemeDto.getName();
    }

    @Override
    public PensionSchemeDto getDto() {
        return new ModelMapper().map(this, PensionSchemeDto.class);
    }
}
