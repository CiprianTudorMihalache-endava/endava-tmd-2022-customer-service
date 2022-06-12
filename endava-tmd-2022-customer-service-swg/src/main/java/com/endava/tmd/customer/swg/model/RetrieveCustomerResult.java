package com.endava.tmd.customer.swg.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RetrieveCustomerResult {
    @Schema(description = "Database identifier of the customer record", example = "123")
    private Long id;

    @Schema(description = "Database version of the customer record", example = "3")
    private Long version;

    @Schema(description = "Creation date and time of the customer record", example = "2022-06-15T19:06:22.628085Z")
    private OffsetDateTime createDateTime;

    @Schema(description = "Last update date and time of the customer record", example = "2022-06-15T19:23:11.582712Z")
    private OffsetDateTime lastUpdateDateTime;

    @Schema(description = "First name of the customer", example = "James")
    private String firstName;

    @Schema(description = "Last name of the customer", example = "Bond")
    private String lastName;

    @Schema(description = "Birth date of the customer", example = "1980-07-20")
    private LocalDate dateOfBirth;

    @Schema(description = "Security questions")
    private Map<String, String> securityQuestions = new HashMap<>();
}
