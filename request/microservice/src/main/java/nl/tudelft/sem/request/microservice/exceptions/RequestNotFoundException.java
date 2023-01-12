package nl.tudelft.sem.request.microservice.exceptions;

/**
 * Exception thrown when a request is not found.
 */
public class RequestNotFoundException extends NotFoundException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor for RequestNotFoundException.
     *
     * @param message exception message.
     */
    public RequestNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for RequestNotFoundException.
     *
     * @param e Exception thrown.
     */
    public RequestNotFoundException(Exception e) {
        super(e);
    }

    /**
     * Constructor for RequestNotFoundException.
     */
    public RequestNotFoundException() {
        this("Request not found");
    }
}
