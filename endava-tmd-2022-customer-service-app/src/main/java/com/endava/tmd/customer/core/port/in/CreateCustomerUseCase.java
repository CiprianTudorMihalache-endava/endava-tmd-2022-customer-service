package com.endava.tmd.customer.core.port.in;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;

import lombok.Value;

public interface CreateCustomerUseCase {

    CreateCustomerResponse create(CreateCustomerCommand command);

    @Value
    class CreateCustomerCommand {
        private CreateCustomerRequest request;
    }

}
