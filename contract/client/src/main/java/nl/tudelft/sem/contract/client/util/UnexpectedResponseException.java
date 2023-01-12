package nl.tudelft.sem.contract.client.util;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Exception class used to wrap unexpected responses (e.g. 500 Internal Server Error) - improperly handled remote errors.
 */
public class UnexpectedResponseException extends Exception {
    private static final long serialVersionUID = 1L;

    @Getter
    @Accessors(fluent = true)
    public final int statusCode;

    /**
     * Create a new UnexpectedResponseException.
     *
     * @param statusCode the status code of the response.
     * @param message the message of the exception.
     */
    public UnexpectedResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Create a new UnexpectedResponseException.
     *
     * @param statusCode the status code of the response.
     * @param cause the cause of the exception.
     */
    public UnexpectedResponseException(int statusCode, Exception cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    /**
     * Create a new UnexpectedResponseException.
     *
     * @param statusCode the status code of the response.
     * @param message the message of the exception.
     * @param cause the cause of the exception.
     */
    public UnexpectedResponseException(int statusCode, String message, Exception cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
