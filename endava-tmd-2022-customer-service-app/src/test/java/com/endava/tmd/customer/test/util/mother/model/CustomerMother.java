package com.endava.tmd.customer.test.util.mother.model;

import java.time.LocalDate;

import com.endava.tmd.customer.model.Customer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomerMother {

    public static Customer peterPan() {
        return new Customer()
                .setFirstName("Peter")
                .setLastName("Pan")
                .setDateOfBirth(LocalDate.parse("2000-11-22"));
    }

}
