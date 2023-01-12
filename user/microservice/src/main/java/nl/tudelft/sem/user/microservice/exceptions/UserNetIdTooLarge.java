package nl.tudelft.sem.user.microservice.exceptions;

public abstract class UserNetIdTooLarge extends IllegalStateException {
    public static final long serialVersionUID = 1L;

    public UserNetIdTooLarge(String message) {
        super(message);
    }

    public UserNetIdTooLarge(Exception e) {
        super(e);
    }

    public UserNetIdTooLarge() {
        this("NetId has to be less than 20 characters");
    }
}
