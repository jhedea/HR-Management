package nl.tudelft.sem.request.commons.entities;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.request.commons.entities.utils.Views;

@NoArgsConstructor
@Builder
@JsonView(Views.Public.class)
public class ResponseRequestDto extends RequestDto {
}
