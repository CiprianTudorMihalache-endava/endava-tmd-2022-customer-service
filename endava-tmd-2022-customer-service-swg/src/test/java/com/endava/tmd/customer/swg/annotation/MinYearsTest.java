package com.endava.tmd.customer.swg.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;

class MinYearsTest {
    private Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void validValue() {
        final var testClass = new TestClass(LocalDate.now().minusYears(21));
        assertThat(validator.validate(testClass)).isEmpty();
    }

    @Test
    void invalidValue() {
        final var testClass = new TestClass(LocalDate.now().minusYears(21).plusDays(1));
        final Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);
        assertThat(violations).singleElement().satisfies(v -> {
            assertThat(v.getPropertyPath()).hasToString("ld");
            assertThat(v.getMessage()).isEqualTo("value must be older or equal than 21 years in the past");
        });
    }

    @AllArgsConstructor
    private static class TestClass {
        @MinYears(21)
        LocalDate ld;
    }

}
