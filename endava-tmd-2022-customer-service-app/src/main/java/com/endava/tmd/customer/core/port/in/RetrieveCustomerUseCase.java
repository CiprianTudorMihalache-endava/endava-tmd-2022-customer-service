package com.endava.tmd.customer.core.port.in;

import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

import lombok.Value;

public interface RetrieveCustomerUseCase {

    RetrieveCustomerResponse retrieve(RetrieveCustomerCommand command);

    @Value
    class RetrieveCustomerCommand {
        private long customerId;
    }

}
