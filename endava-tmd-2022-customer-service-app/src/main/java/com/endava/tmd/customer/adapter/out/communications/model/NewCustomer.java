package com.endava.tmd.customer.adapter.out.communications.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NewCustomer {
    private Long customerId;
    private String firstName;
    private LocalDate dateOfBirth;
}
