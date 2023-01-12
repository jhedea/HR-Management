package nl.tudelft.sem.user.microservice.exceptions;

public abstract class UserNetIdCannotBeEmpty extends IllegalStateException {
    public static final long serialVersionUID = 1L;

    public UserNetIdCannotBeEmpty(String message) {
        super(message);
    }

    public UserNetIdCannotBeEmpty(Exception e) {
        super(e);
    }

    public UserNetIdCannotBeEmpty() {
        this("NetId cannot be empty");
    }
}
