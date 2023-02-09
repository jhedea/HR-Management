package nl.tudelft.sem.user.commons.entities.utils;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class UuidDto implements Dto {
    private transient UUID uuid;
}
