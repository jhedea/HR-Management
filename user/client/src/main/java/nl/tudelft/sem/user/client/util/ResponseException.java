package nl.tudelft.sem.user.client.util;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.tudelft.sem.user.commons.ApiError;

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
        super();
        this.error = error;
    }


}
