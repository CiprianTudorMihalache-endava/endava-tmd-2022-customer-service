package com.endava.tmd.customer.swg.api;

import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

public interface CustomerApi {

    @PostMapping(value = "/v1/customers", produces = {"application/json"})
    ResponseEntity<CreateCustomerResponse> createCustomer(@Validated @RequestBody CreateCustomerRequest request);
    // We could validate it using the @Validated annotation with the same effect

    @GetMapping(value = "/v1/customers/{customerId}", produces = {"application/json"})
    ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(@PathVariable("customerId") @Min(value = 1,
            message = "customerId must be greater than or equal to 1") long customerId);

}
