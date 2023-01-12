package nl.tudelft.sem.contract.microservice.exceptions;


/**
 * Exception for when Job Position is not found.
 */
public class JobPositionNotFoundException extends NotFoundException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor with explicit message.
     *
     * @param message exception message
     */
    public JobPositionNotFoundException(String message) {
        super(message);
    }

    /**
     * Generic constructor JobPositionNotFoundException.
     */
    public JobPositionNotFoundException() {
        this("Job position not found");
    }
}
