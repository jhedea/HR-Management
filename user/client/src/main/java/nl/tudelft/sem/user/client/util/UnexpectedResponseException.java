package nl.tudelft.sem.user.client.util;

import lombok.Getter;
import lombok.experimental.Accessors;

public class UnexpectedResponseException extends Exception {
    private static final long serialVersionUID = 1L;

    @Getter
    @Accessors(fluent = true)
    public final int statusCode;

    /**
     * Create a new UnexpectedResponseException.
     *
     * @param statusCode the status code of the response.
     * @param message    the message of the exception.
     */
    public UnexpectedResponseException(int statusCode, String message) {
        super();
        this.statusCode = statusCode;
    }

    /**
     * Create a new UnexpectedResponseException.
     *
     * @param statusCode the status code of the response.
     * @param cause      the cause of the exception.
     */
    public UnexpectedResponseException(int statusCode, Exception cause) {
        super();
        this.statusCode = statusCode;
    }

    /**
     * Create a new UnexpectedResponseException.
     *
     * @param statusCode the status code of the response.
     * @param message    the message of the exception.
     * @param cause      the cause of the exception.
     */
    public UnexpectedResponseException(int statusCode, String message, Exception cause) {
        super();
        this.statusCode = statusCode;
    }
}
