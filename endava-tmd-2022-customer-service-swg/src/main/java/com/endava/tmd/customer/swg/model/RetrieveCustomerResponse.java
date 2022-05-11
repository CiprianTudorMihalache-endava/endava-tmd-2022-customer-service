package com.endava.tmd.customer.swg.model;

import lombok.Data;

@Data
public class RetrieveCustomerResponse {
    private String message;
    private String traceId;
    private String buildVersion;

    private String firstName;
    private String lastName;
}
