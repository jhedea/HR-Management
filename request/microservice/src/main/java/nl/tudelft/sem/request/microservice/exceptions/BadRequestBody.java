package nl.tudelft.sem.request.microservice.exceptions;

public class BadRequestBody extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public BadRequestBody(String errMessage) {
        super(errMessage);
    }

    public BadRequestBody() {
        super("The Body of the Request is Illegal.");
    }

    public BadRequestBody(Exception e) {
        super(e);
    }
}
