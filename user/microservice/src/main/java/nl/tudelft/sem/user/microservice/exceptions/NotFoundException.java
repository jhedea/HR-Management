package nl.tudelft.sem.user.microservice.exceptions;

public abstract class NotFoundException extends IllegalStateException {
    public static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Exception e) {
        super(e);
    }

    public NotFoundException() {
        this("Not found");
    }
}

