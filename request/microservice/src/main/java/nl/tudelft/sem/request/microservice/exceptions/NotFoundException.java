package nl.tudelft.sem.request.microservice.exceptions;

public abstract class NotFoundException extends IllegalStateException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor for RequestNotFoundException.
     *
     * @param message exception message.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for RequestNotFoundException.
     *
     * @param e Exception thrown.
     */
    public NotFoundException(Exception e) {
        super(e);
    }

    /**
     * Default constructor for RequestNotFoundException.
     */
    public NotFoundException() {
        this("Not found");
    }
}
