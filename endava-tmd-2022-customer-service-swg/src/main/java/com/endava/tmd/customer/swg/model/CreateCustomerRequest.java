package com.endava.tmd.customer.swg.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.endava.tmd.customer.swg.annotation.MinYears;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCustomerRequest {
    // Standard validations refresher: https://mossgreen.github.io/Validations-in-Spring/
    // Swagger documentation basics: https://waynestalk.com/en/springdoc-openapi-tutorial-en/

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "First name of the customer", example = "James")
    private String firstName;

    @Size(max = 50)
    @Schema(description = "Last name of the customer", example = "Bond")
    private String lastName;

    @MinYears(18)
    @Schema(description = "Birth date of the customer", example = "1980-07-20")
    private LocalDate dateOfBirth;

    @Schema(description = "Security questions")
    private Map<String, String> securityQuestions = new HashMap<>();

}
