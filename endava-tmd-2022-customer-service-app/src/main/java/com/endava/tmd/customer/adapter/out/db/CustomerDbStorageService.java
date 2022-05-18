package com.endava.tmd.customer.adapter.out.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.tmd.customer.core.port.out.CustomerStorage;
import com.endava.tmd.customer.model.Customer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerDbStorageService implements CustomerStorage {
    private final List<Customer> customers = new ArrayList<>();

    @Override
    public Customer save(final Customer customer) {
        // of course it is a very dumb implementation
        customer.setId(customers.size() + 1L);
        customers.add(customer);
        return customer;
    }

    @Override
    public Optional<Customer> findById(final Long customerId) {
        if (customerId > customers.size()) {
            return Optional.empty();
        }
        return Optional.of(customers.get(customerId.intValue() - 1));
    }

}
