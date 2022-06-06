package com.endava.tmd.customer.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Map<String, String> securityQuestions = new HashMap<>();
}
