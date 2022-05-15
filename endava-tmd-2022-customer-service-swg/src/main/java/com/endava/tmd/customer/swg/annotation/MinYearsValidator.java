package com.endava.tmd.customer.swg.annotation;

import java.time.LocalDate;
import java.time.Period;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinYearsValidator implements ConstraintValidator<MinYears, LocalDate> {
    private int minYears;

    @Override
    public void initialize(final MinYears minYearsAnnotation) {
        minYears = minYearsAnnotation.value();
    }

    @Override
    public boolean isValid(final LocalDate value, final ConstraintValidatorContext context) {
        return value != null && Period.between(value, LocalDate.now()).getYears() >= minYears;
    }

}
