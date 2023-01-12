package nl.tudelft.sem.contract.microservice.exceptions;

/**
 * Exception when salary scale not found.
 */
public class SalaryScaleNotFoundException extends NotFoundException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor with explicit message.
     *
     * @param message exception message
     */
    public SalaryScaleNotFoundException(String message) {
        super(message);
    }

    /**
     * Generic constructor SalaryScaleNotFoundException.
     */
    public SalaryScaleNotFoundException() {
        this("Salary scale not found");
    }
}

