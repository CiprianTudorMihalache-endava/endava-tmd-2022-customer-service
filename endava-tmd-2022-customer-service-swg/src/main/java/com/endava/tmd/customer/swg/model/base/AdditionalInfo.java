package com.endava.tmd.customer.swg.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdditionalInfo {
    @Schema(description = "The information's message")
    private String message;
}
