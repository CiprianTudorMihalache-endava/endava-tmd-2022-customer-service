package com.endava.tmd.customer.swg.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RetrieveCustomerResult {
    @Schema(description = "First name of the customer", example = "James")
    private String firstName;

    @Schema(description = "Last name of the customer", example = "Bond")
    private String lastName;

    @Schema(description = "Birth date of the customer", example = "1980-07-20")
    private LocalDate dateOfBirth;
}
