package com.endava.tmd.customer.swg.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
        final var request = validCreateCustomerRequest();
        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void emptyFirstName() {
        final var request = validCreateCustomerRequest()
                .setFirstName("");
        assertThat(validator.validate(request))
                .map(extractMessage())
                .containsExactlyInAnyOrder("firstName must not be blank", "firstName size must be between 1 and 50");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void blankFirstName(final String firstName) {
        final var request = validCreateCustomerRequest()
                .setFirstName(firstName);
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("firstName must not be blank");
    }

    @Test
    void tooLargeFirstName() {
        final var request = validCreateCustomerRequest()
                .setFirstName("x".repeat(51));
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("firstName size must be between 1 and 50");
    }

    @Test
    void tooLargeLastName() {
        final var request = validCreateCustomerRequest()
                .setLastName("x".repeat(51));
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("lastName size must be between 0 and 50");
    }

    @Test
    void tooYoung() {
        final var request = validCreateCustomerRequest()
                .setDateOfBirth(LocalDate.now().minusYears(18).plusDays(1));
        assertThat(validator.validate(request)).singleElement()
                .extracting(extractMessage())
                .isEqualTo("dateOfBirth value must be older or equal than 18 years in the past");
    }

    private Function<? super ConstraintViolation<CreateCustomerRequest>, String> extractMessage() {
        return cv -> String.join(" ", cv.getPropertyPath().toString(), cv.getMessage());
    }

    private CreateCustomerRequest validCreateCustomerRequest() {
        return new CreateCustomerRequest()
                .setFirstName("Peter")
                .setLastName("Pan")
                .setDateOfBirth(LocalDate.now().minusYears(18));
    }

}
