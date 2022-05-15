package com.endava.tmd.customer.swg.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RetrieveCustomerResult {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
