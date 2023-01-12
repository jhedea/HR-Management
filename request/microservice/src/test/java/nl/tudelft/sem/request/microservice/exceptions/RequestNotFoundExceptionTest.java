package nl.tudelft.sem.request.microservice.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class RequestNotFoundExceptionTest {
    @Test
    void defaultConstructor() {
        RequestNotFoundException exception = new RequestNotFoundException();
        assertNotNull(exception.getMessage());
    }

    @Test
    void causeConstructor() {
        Exception e = new Exception();
        RequestNotFoundException exception = new RequestNotFoundException(e);
        assertEquals(e, exception.getCause());
    }

    @Test
    void messageConstructor() {
        String message = "message";
        RequestNotFoundException exception = new RequestNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }
}