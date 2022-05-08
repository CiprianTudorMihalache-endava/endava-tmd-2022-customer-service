package com.endava.tmd.customer.swg.model;

import lombok.Data;

@Data
public class CreateCustomerResponse {
    private String message;
    private String traceId;
    private String buildVersion;
}
