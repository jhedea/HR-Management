package nl.tudelft.sem.user.microservice.exceptions;

public class UserNotFoundException extends NotFoundException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor for UserNotFoundException.
     *
     * @param message exception message.
     */
    public UserNotFoundException(String message) {
        super();
    }

    /**
     * Constructor for UserNotFoundException.
     */
    public UserNotFoundException() {
        this("User not found");
    }
}
