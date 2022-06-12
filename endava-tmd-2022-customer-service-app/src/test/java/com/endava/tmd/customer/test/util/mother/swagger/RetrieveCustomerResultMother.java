package com.endava.tmd.customer.test.util.mother.swagger;

import java.time.LocalDate;
import java.util.Map;

import com.endava.tmd.customer.swg.model.RetrieveCustomerResult;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetrieveCustomerResultMother {

    public static RetrieveCustomerResult peterPan(final Long id, final Long version) {
        return new RetrieveCustomerResult()
                .setId(id)
                .setVersion(version)
                .setFirstName("Peter")
                .setLastName("Pan")
                .setDateOfBirth(LocalDate.parse("2000-11-22"))
                .setSecurityQuestions(Map.of(
                        "What was your age when you were born?", "0.75",
                        "What was the place where you were born?", "table"));
    }

    public static RetrieveCustomerResult jamesBond() {
        return new RetrieveCustomerResult()
                .setId(1L)
                .setVersion(0L)
                .setFirstName("James")
                .setLastName("Bond")
                .setDateOfBirth(LocalDate.parse("2002-02-20"))
                .setSecurityQuestions(Map.of(
                        "Who are you?", "me",
                        "Write Your Password", "Your Password"));
    }

}
