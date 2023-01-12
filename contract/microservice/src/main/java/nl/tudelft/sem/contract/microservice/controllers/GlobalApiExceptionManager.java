package nl.tudelft.sem.contract.microservice.controllers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.commons.ApiError;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import nl.tudelft.sem.contract.microservice.exceptions.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalApiExceptionManager {
    /**
     * Handles validation errors for API fields annotated with @Valid.
     *
     * @param ex caught exception
     * @return API error
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        ApiError error = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error");
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            error.addError(fieldError.getDefaultMessage());
        }
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler({NotFoundException.class})
    public ApiError handleNotFoundException(NotFoundException ex) {
        log.warn("Not found exception: ({}) {}", ex.getClass().getSimpleName(), ex.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler({ActionNotAllowedException.class})
    public ApiError handleActionNotAllowedException(ActionNotAllowedException ex) {
        log.warn("Not allowed exception: ({}) {}", ex.getClass().getSimpleName(), ex.getMessage());
        return new ApiError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
