package com.endava.tmd.customer.core.service;

import org.springframework.stereotype.Service;

import com.endava.tmd.customer.core.converter.CustomerToRetrieveCustomerResultConverter;
import com.endava.tmd.customer.core.exception.RetrieveCustomerException;
import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.core.port.in.RetrieveCustomerUseCase;
import com.endava.tmd.customer.core.port.out.CustomerStorage;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrieveCustomerService implements RetrieveCustomerUseCase {
    private final CustomerToRetrieveCustomerResultConverter modelToResultConverter;
    private final CustomerStorage storage;
    private final ResponseFactory responseFactory;

    @Override
    public RetrieveCustomerResponse retrieve(final RetrieveCustomerCommand command) {
        final var customerId = command.getCustomerId();
        log.debug("Attempting to find customer with id {}", customerId);
        return storage.findById(customerId)
                .map(modelToResultConverter::convert)
                .map(result -> responseFactory.build(RetrieveCustomerResponse::new,
                        "Retrieve operation was successfully processed", result))
                .orElseThrow(() -> new RetrieveCustomerException(customerId));
    }

}
