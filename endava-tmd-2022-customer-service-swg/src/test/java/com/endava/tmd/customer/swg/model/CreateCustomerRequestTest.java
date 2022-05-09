package com.endava.tmd.customer.swg.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class CreateCustomerRequestTest {
    private Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void validRequest() {
        final var request = new CreateCustomerRequest()
                .setFirstName("Peter");
        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void emptyFirstName() {
        final var request = new CreateCustomerRequest()
                .setFirstName("");
        assertThat(validator.validate(request))
                .map(extractMessage())
                .containsExactlyInAnyOrder("firstName must not be blank", "firstName size must be between 1 and 50");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void blankFirstName(final String firstName) {
        final var request = new CreateCustomerRequest()
                .setFirstName(firstName);
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("firstName must not be blank");
    }

    @Test
    void tooLargeFirstName() {
        final var request = new CreateCustomerRequest()
                .setFirstName("x".repeat(51));
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("firstName size must be between 1 and 50");
    }

    @Test
    void tooLargeLastName() {
        final var request = new CreateCustomerRequest()
                .setFirstName("Peter")
                .setLastName("x".repeat(51));
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("lastName size must be between 0 and 50");
    }

    private Function<? super ConstraintViolation<CreateCustomerRequest>, String> extractMessage() {
        return cv -> String.join(" ", cv.getPropertyPath().toString(), cv.getMessage());
    }

}
