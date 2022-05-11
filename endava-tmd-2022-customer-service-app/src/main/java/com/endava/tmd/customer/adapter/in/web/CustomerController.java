package com.endava.tmd.customer.adapter.in.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.endava.tmd.customer.swg.api.CustomerApi;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CustomerController implements CustomerApi {
    private final BuildProperties buildProperties;
    private List<CreateCustomerRequest> customers = new ArrayList<>();

    @Override
    public ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        log.info("Attempting to create customer: {}", request);
        customers.add(request);
        final var response = new CreateCustomerResponse()
                .setMessage("Customer created successfully")
                .setCustomerId((long) customers.size())
                .setTraceId(MDC.get("traceId"))
                .setBuildVersion(buildProperties.getVersion());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(final long customerId) {
        if (customerId > customers.size()) {
            final var response = new RetrieveCustomerResponse()
                    .setMessage("Cannot find customer with id = " + customerId)
                    .setTraceId(MDC.get("traceId"))
                    .setBuildVersion(buildProperties.getVersion());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        final var customer = customers.get((int) customerId - 1);
        final var response = new RetrieveCustomerResponse()
                .setMessage("Retrieve operation was successfully processed")
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setTraceId(MDC.get("traceId"))
                .setBuildVersion(buildProperties.getVersion());
        // this form is not so powerful
        // it cannot be used to return a 404 NOT FOUND with a body
        return ResponseEntity.ok(response);
    }

}
