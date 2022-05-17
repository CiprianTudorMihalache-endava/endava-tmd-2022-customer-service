package com.endava.tmd.customer.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Customer {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
