package com.endava.tmd.customer.test.util.mother.model;

import java.time.LocalDate;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.endava.tmd.customer.model.Customer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomerMother {

    public static Customer peterPan() {
        return new Customer()
                .setFirstName("Peter")
                .setLastName("Pan")
                .setDateOfBirth(LocalDate.parse("2000-11-22"))
                .setSecurityQuestions(Map.of(
                        "What was your age when you were born?", "0.75",
                        "What was the place where you were born?", "table"));
    }

    public static Customer peterPanPersisted(final Long id, final Long version) {
        return setVersion(peterPan(), version)
                .setId(id);
    }

    public static Customer jamesBond() {
        return setVersion(new Customer(), 0L)
                .setId(1L)
                .setFirstName("James")
                .setLastName("Bond")
                .setDateOfBirth(LocalDate.parse("2002-02-20"))
                .setSecurityQuestions(Map.of(
                        "Who are you?", "me",
                        "Write Your Password", "Your Password"));
    }

    private static Customer setVersion(final Customer customer, final Long version) {
        try {
            FieldUtils.writeField(customer, "version", version, true);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

}
