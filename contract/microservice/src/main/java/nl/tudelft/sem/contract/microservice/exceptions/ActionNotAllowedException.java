package nl.tudelft.sem.contract.microservice.exceptions;

public class ActionNotAllowedException extends IllegalStateException {
    public static final long serialVersionUID = 1L;

    /**
     * Action not allowed constructor.
     *
     * @param message exception message
     */
    public ActionNotAllowedException(String message) {
        super(message);
    }

    /**
     * Action not allowed constructor with generic message.
     */
    public ActionNotAllowedException() {
        this("Action not allowed");
    }
}
