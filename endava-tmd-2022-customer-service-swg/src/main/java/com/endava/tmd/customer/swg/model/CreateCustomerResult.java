package com.endava.tmd.customer.swg.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCustomerResult {
    @Schema(description = "The identifier of the newly created customer", example = "1234")
    private Long customerId;
}
