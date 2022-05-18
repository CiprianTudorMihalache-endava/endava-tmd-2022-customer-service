package com.endava.tmd.customer.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.endava.tmd.customer.core.port.in.CreateCustomerUseCase;
import com.endava.tmd.customer.core.port.in.CreateCustomerUseCase.CreateCustomerCommand;
import com.endava.tmd.customer.core.port.in.RetrieveCustomerUseCase;
import com.endava.tmd.customer.core.port.in.RetrieveCustomerUseCase.RetrieveCustomerCommand;
import com.endava.tmd.customer.swg.api.CustomerApi;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class CustomerController implements CustomerApi {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final RetrieveCustomerUseCase retrieveCustomerUseCase;

    @Override
    public ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        final var response = createCustomerUseCase.create(new CreateCustomerCommand(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(final long customerId) {
        final var response = retrieveCustomerUseCase.retrieve(new RetrieveCustomerCommand(customerId));
        return ResponseEntity.ok(response);
    }

}
