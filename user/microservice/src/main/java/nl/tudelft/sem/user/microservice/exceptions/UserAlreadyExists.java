package nl.tudelft.sem.user.microservice.exceptions;

public abstract class UserAlreadyExists extends IllegalStateException {
    public static final long serialVersionUID = 1L;

    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists(Exception e) {
        super(e);
    }

    /// return 409 if user already exists
    public UserAlreadyExists() {
        this("Already used");
    }
}
