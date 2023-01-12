package nl.tudelft.sem.request.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.request.commons.ApiError;
import nl.tudelft.sem.request.commons.entities.utils.Dto;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;


@RequiredArgsConstructor
public class RequestFutureCallback<T extends Dto> implements FutureCallback<SimpleHttpResponse> {
    /**
     * The future to complete.
     */
    @NonNull
    private final CompletableFuture<T> future;

    /**
     * The type of the result object DTO.
     */
    @NonNull
    private final Class<T> responseType;

    /**
     * The mapper to use.
     */
    @NonNull
    private final ObjectMapper mapper;

    /**
     * Handler when the future successfully completes.
     *
     * @param result the result object received from the HTTP client.
     */
    @Override
    public void completed(SimpleHttpResponse result) {
        // If we didn't get a successful response, we should throw an exception.
        int responseCode = result.getCode();
        if (responseCode < 200 || responseCode >= 300) {
            try {
                // Try to parse the error and throw the ResponseException.
                future.completeExceptionally(
                        new ResponseException(mapper.readValue(result.getBodyText(), ApiError.class)));
            } catch (Exception e) {
                // If we can't parse the error, throw a generic exception.
                future.completeExceptionally(
                        new UnexpectedResponseException(responseCode, result.getBodyText(), e));
            }
        } else {
            try {
                future.complete(mapper.readValue(result.getBodyText(), responseType));
            } catch (Exception e) {
                future.completeExceptionally(
                        new UnexpectedResponseException(responseCode, result.getBodyText(), e));
            }
        }
    }

    /**
     * Handler when the future fails.
     *
     * @param ex the exception that occurred.
     */
    @Override
    public void failed(Exception ex) {
        future.completeExceptionally(ex);
    }

    /**
     * Handler when the future is cancelled.
     */
    @Override
    public void cancelled() {
        future.cancel(true);
    }
}
