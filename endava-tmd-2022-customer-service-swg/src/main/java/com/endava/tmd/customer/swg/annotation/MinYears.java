package com.endava.tmd.customer.swg.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(FIELD)
@Documented
@Constraint(validatedBy = MinYearsValidator.class)
// if this class is completely alien for you: https://web-engineering.info/tech/JavaJpaJsf/book/ch06s07.html
public @interface MinYears {
    String message() default "value must be older or equal than {value} years in the past";

    int value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
