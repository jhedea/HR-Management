package nl.tudelft.sem.notification.commons.entities;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.notification.commons.entities.utils.Dto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UuidDto implements Dto {
    private UUID uuid;
}
