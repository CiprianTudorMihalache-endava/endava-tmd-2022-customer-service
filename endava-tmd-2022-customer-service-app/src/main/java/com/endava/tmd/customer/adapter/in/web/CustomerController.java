package com.endava.tmd.customer.adapter.in.web;

import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.endava.tmd.customer.swg.api.CustomerApi;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {
    private final BuildProperties buildProperties;

    @Override
    public ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        log.info("Attempting to create customer: {}", request);
        final var response = new CreateCustomerResponse()
                .setMessage("Customer created successfully")
                .setTraceId(MDC.get("traceId"))
                .setBuildVersion(buildProperties.getVersion());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
