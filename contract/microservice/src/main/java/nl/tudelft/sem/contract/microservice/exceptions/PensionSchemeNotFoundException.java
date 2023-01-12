package nl.tudelft.sem.contract.microservice.exceptions;

/**
 * Exception for when the pension scheme is not found.
 */
public class PensionSchemeNotFoundException extends NotFoundException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor with explicit message.
     *
     * @param message exception message
     */
    public PensionSchemeNotFoundException(String message) {
        super(message);
    }

    /**
     * Generic constructor.
     */
    public PensionSchemeNotFoundException() {
        this("Pension scheme not found");
    }

}
