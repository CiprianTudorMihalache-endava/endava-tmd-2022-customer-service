package com.endava.tmd.customer.core.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.swg.model.base.GenericResponse;

@ExtendWith(MockitoExtension.class)
class CustomerExceptionHandlerTest {
    private static final GenericResponse RESPONSE = new GenericResponse();
    private static final String MESSAGE = "issue description";

    @Mock
    private ResponseFactory responseFactory;

    @InjectMocks
    private CustomerExceptionHandler exceptionHandler;

    @Test
    void illegalArgumentException() {
        final var exception = new IllegalArgumentException(MESSAGE);
        when(responseFactory.build(MESSAGE)).thenReturn(RESPONSE);
        assertThat(exceptionHandler.badRequestExceptions(exception)).isSameAs(RESPONSE);
    }

    @Test
    void httpMessageNotReadableException() {
        final var exception = new HttpMessageNotReadableException("NOT this error message",
                new RuntimeException(MESSAGE), mock(HttpInputMessage.class));
        when(responseFactory.build(MESSAGE)).thenReturn(RESPONSE);
        assertThat(exceptionHandler.badRequestNestedExceptions(exception)).isSameAs(RESPONSE);
    }

    @Test
    void methodArgumentNotValidExceptionWithOneFieldError() {
        final var bindingResult = mock(BindingResult.class);
        final var methodParameter = mock(MethodParameter.class);

        // Mockito cannot mock sealed classes: https://stackoverflow.com/a/68435990/2032694
        // Thus, it is not possible to define mockExecutable as mock(Executable.class)
        // Mockito cannot mock final classes also
        // Thus, it is not possible to define mockExecutable as mock(Method.class)
        // We can find a Method, but that one will be displayed in logs :D
        final var mockExecutable = CustomerExceptionHandlerTest.class.getMethods()[0];

        final var errorMessage = "must be greater or equal to 18";

        when(methodParameter.getParameterIndex()).thenReturn(0);
        when(methodParameter.getExecutable()).thenReturn(mockExecutable);
        // since we are unable to mock executable, we cannot have control over the result of the "toGenericString" method
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("myObj", "age", errorMessage)));
        when(responseFactory.build(List.of("age must be greater or equal to 18"))).thenReturn(RESPONSE);

        final var exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        assertThat(exceptionHandler.handle(exception)).isSameAs(RESPONSE);
    }

    @Test
    void constraintViolationException() {
        final var violations = Set.of(createConstraintViolation("myObj.age", MESSAGE));
        final var exception = new ConstraintViolationException("abc", violations);
        when(responseFactory.build(List.of("age fails constraint validation: " + MESSAGE))).thenReturn(RESPONSE);
        assertThat(exceptionHandler.handle(exception)).isSameAs(RESPONSE);
    }

    @Test
    void retrieveCustomerException() {
        final var exception = new RetrieveCustomerException(123L);
        when(responseFactory.build(exception.getMessage())).thenReturn(RESPONSE);
        assertThat(exceptionHandler.handle(exception)).isSameAs(RESPONSE);
    }

    @Test
    void illegalStateException() {
        final var exception = new IllegalStateException(MESSAGE);
        when(responseFactory.build(MESSAGE)).thenReturn(RESPONSE);
        assertThat(exceptionHandler.handle(exception)).isSameAs(RESPONSE);
    }

    // of course we should add at least one test for each exception type defined in the handler

    private <T> ConstraintViolation<T> createConstraintViolation(final String path, final String message) {
        @SuppressWarnings("unchecked")
        final ConstraintViolation<T> mockConstraintViolation = mock(ConstraintViolation.class);
        when(mockConstraintViolation.getPropertyPath()).thenReturn(PathImpl.createPathFromString(path));
        when(mockConstraintViolation.getMessage()).thenReturn(message);
        return mockConstraintViolation;
    }

}
