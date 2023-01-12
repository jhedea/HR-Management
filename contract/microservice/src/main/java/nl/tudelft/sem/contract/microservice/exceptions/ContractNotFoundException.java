package nl.tudelft.sem.contract.microservice.exceptions;

/**
 * Exception thrown when a contract is not found.
 */
public class ContractNotFoundException extends NotFoundException {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor for ContractNotFoundException.
     *
     * @param message exception message.
     */
    public ContractNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for ContractNotFoundException.
     */
    public ContractNotFoundException() {
        this("Contract not found");
    }
}
