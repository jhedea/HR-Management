package nl.tudelft.sem.notification.commons.entities;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.notification.commons.entities.utils.Dto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCollectionDto implements Dto {
    private List<MessageDto> messages;
}
