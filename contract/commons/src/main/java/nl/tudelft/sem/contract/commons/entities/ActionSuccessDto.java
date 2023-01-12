package nl.tudelft.sem.contract.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;

/**
 * Class to denote the success of a request which doesn't return a proper body.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionSuccessDto implements Dto {
    private boolean success = true;
}
