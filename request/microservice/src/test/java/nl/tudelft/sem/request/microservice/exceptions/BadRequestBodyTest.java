package nl.tudelft.sem.request.microservice.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class BadRequestBodyTest {
    @Test
    void defaultConstructor() {
        BadRequestBody exception = new BadRequestBody();
        assertNotNull(exception.getMessage());
    }

    @Test
    void causeConstructor() {
        Exception e = new Exception();
        BadRequestBody exception = new BadRequestBody(e);
        assertEquals(e, exception.getCause());
    }

    @Test
    void messageConstructor() {
        String message = "message";
        BadRequestBody exception = new BadRequestBody(message);
        assertEquals(message, exception.getMessage());
    }
}