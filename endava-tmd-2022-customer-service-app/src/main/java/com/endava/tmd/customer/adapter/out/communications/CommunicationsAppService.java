package com.endava.tmd.customer.adapter.out.communications;

import org.springframework.stereotype.Service;

import com.endava.tmd.customer.adapter.out.communications.model.NewCustomer;
import com.endava.tmd.customer.core.port.out.CommunicationsApp;
import com.endava.tmd.customer.model.Customer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunicationsAppService implements CommunicationsApp {
    private final CommunicationsAppClient appClient;

    @Override
    public void reportNewCustomer(final Customer customer) {
        final var newCustomer = new NewCustomer()
                .setCustomerId(customer.getId())
                .setFirstName(customer.getFirstName())
                .setDateOfBirth(customer.getDateOfBirth());
        appClient.reportNewCustomer(newCustomer);
    }

}
