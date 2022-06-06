package com.endava.tmd.customer.test.util.mother.swagger;

import java.time.LocalDate;
import java.util.Map;

import com.endava.tmd.customer.swg.model.RetrieveCustomerResult;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetrieveCustomerResultMother {

    public static RetrieveCustomerResult peterPan() {
        return new RetrieveCustomerResult()
                .setFirstName("Peter")
                .setLastName("Pan")
                .setDateOfBirth(LocalDate.parse("2000-11-22"))
                .setSecurityQuestions(Map.of(
                        "What was your age when you were born?", "0.75",
                        "What was the place where you were born?", "table"));
    }

}
