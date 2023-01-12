package nl.tudelft.sem.notification.client.util;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.tudelft.sem.notification.commons.ApiError;

/**
 * Exception class used to wrap API errors (properly-handled remote errors).
 */
public class ResponseException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Wrapped ApiError object instance.
     */
    @Accessors(fluent = true)
    @Getter
    private final ApiError error;

    /**
     * Create a new ResponseError exception.
     *
     * @param error the error to wrap.
     */
    public ResponseException(ApiError error) {
        super(error.getDescription());
        this.error = error;
    }
}
