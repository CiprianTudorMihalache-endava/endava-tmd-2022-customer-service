package com.endava.tmd.customer.core.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.swg.model.base.GenericResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@ResponseBody
@Slf4j
@RequiredArgsConstructor
public class CustomerExceptionHandler {
    // If you are not familiar with exception handling in Spring Web applications, a good introduction is:
    // https://developpaper.com/how-to-implement-custom-exception-of-rest-api-with-spring-and-spring-boot/

    private final ResponseFactory responseFactory;

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            TypeMismatchException.class,
            ServletRequestBindingException.class,
            NoHandlerFoundException.class
    })
    protected GenericResponse badRequestExceptions(final Exception exception) {
        // This is not our problem, thus we may not want a higher log level (error)
        log.info("Bad request exception occurred", exception);
        return responseFactory.build(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    protected GenericResponse badRequestNestedExceptions(final NestedRuntimeException exception) {
        log.info("Bad request nested exception occurred", exception);
        return responseFactory.build(exception.getMostSpecificCause().getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    protected GenericResponse handle(final MethodArgumentNotValidException exception) {
        log.info("MethodArgumentNotValidException occured", exception);
        return responseFactory.build(combineErrorMessages(exception));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    protected GenericResponse handle(final ConstraintViolationException exception) {
        log.info("ConstraintViolationException occured", exception);
        if (exception.getConstraintViolations() != null) {
            return responseFactory.build(
                    // https://stackoverflow.com/a/58850754/2032694
                    exception.getConstraintViolations().stream()
                            .map(cv -> String.format("%s fails constraint validation: %s",
                                    StreamSupport.stream(cv.getPropertyPath().spliterator(), false)
                                            .reduce((first, second) -> second).orElse(null),
                                    cv.getMessage()))
                            .toList());
        }
        return responseFactory.build("Constraint Violation");
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    protected GenericResponse handle(final RetrieveCustomerException exception) {
        log.info("RetrieveCustomerException occured, with message: {}", exception.getMessage());
        return responseFactory.build(exception.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    protected GenericResponse handle(final Exception exception) {
        log.error("Generic exception occurred", exception);
        return responseFactory.build(exception.getMessage());
    }

    private List<String> combineErrorMessages(final MethodArgumentNotValidException exception) {
        final var errorMessages = new ArrayList<>(exception.getBindingResult().getFieldErrors().stream()
                .map(error -> String.join(" ", error.getField(), error.getDefaultMessage())).toList());
        errorMessages.addAll(exception.getBindingResult().getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return errorMessages;
    }

}
