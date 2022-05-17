package com.endava.tmd.customer.adapter.in.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.endava.tmd.customer.core.converter.CreateCustomerRequestToCustomerConverter;
import com.endava.tmd.customer.core.converter.CustomerToRetrieveCustomerResultConverter;
import com.endava.tmd.customer.core.exception.RetrieveCustomerException;
import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.model.Customer;
import com.endava.tmd.customer.swg.api.CustomerApi;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CustomerController implements CustomerApi {
    private final ResponseFactory responseFactory;
    private final CreateCustomerRequestToCustomerConverter requestToModelConverter;
    private final CustomerToRetrieveCustomerResultConverter modelToResponseConverter;
    private List<Customer> customers = new ArrayList<>();

    @Override
    public ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        log.info("Attempting to create customer: {}", request);
        customers.add(requestToModelConverter.convert(request));
        final var result = new CreateCustomerResult().setCustomerId((long) customers.size());
        final var response = responseFactory.build(CreateCustomerResponse::new, "Customer created successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(final long customerId) {
        if (customerId > customers.size()) {
            throw new RetrieveCustomerException(customerId);
        }
        final var customer = customers.get((int) customerId - 1);
        final var result = modelToResponseConverter.convert(customer);
        final var response = responseFactory.build(RetrieveCustomerResponse::new, "Retrieve operation was successfully processed",
                result);
        return ResponseEntity.ok(response);
    }

}
