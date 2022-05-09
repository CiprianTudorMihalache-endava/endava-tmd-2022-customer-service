package com.endava.tmd.customer.swg.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateCustomerRequest {
    // Standard validations refresher: https://mossgreen.github.io/Validations-in-Spring/

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

}
