package com.endava.tmd.customer.swg.api;

import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface CustomerApi {

    @Operation(summary = "Create customer", description = "Adds a new customer")
    @PostMapping(value = "/v1/customers", produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CreateCustomerResponse> createCustomer(@Validated @RequestBody CreateCustomerRequest request);
    // We could validate it using the @Validated annotation with the same effect

    @Operation(summary = "Find customer by id", description = "Finds the only customer with the given id, if exists")
    @GetMapping(value = "/v1/customers/{customerId}", produces = {"application/json"})
    default ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(@Parameter(description = "The id of the customer",
            example = "123") @PathVariable("customerId") @Min(1) final long customerId) {
        // it is still a mystery to me that the default method definition produces correct attribute name in the
        // ConstraintViolationException, but the normal interface abstract method produce the name "arg0"
        // I wonder if it has anything to do with:
        // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-parameter-name-provider
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
