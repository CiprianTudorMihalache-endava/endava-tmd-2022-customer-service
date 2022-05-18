package com.endava.tmd.customer.core.service;

import org.springframework.stereotype.Service;

import com.endava.tmd.customer.core.converter.CreateCustomerRequestToCustomerConverter;
import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.core.port.in.CreateCustomerUseCase;
import com.endava.tmd.customer.core.port.out.CustomerStorage;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCustomerService implements CreateCustomerUseCase {
    private final CreateCustomerRequestToCustomerConverter requestToModelConverter;
    private final CustomerStorage storage;
    private final ResponseFactory responseFactory;

    @Override
    public CreateCustomerResponse create(final CreateCustomerCommand command) {
        log.debug("Processing the create customer command: {}", command);
        final var customer = requestToModelConverter.convert(command.getRequest());
        log.debug("Attempting to create the customer: {}", customer);
        final var savedCustomerId = storage.save(customer).getId();
        log.info("Successfully saved customer with id: {}", savedCustomerId);
        final var result = new CreateCustomerResult().setCustomerId(savedCustomerId);
        return responseFactory.build(CreateCustomerResponse::new, "Customer created successfully", result);
    }

}
