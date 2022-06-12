package com.endava.tmd.customer.adapter.out.db;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.tmd.customer.core.port.out.CustomerStorage;
import com.endava.tmd.customer.model.Customer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerDbStorageService implements CustomerStorage {
    private final CustomerRepository repository;

    @Override
    public Customer save(final Customer customer) {
        return repository.save(customer);
    }

    @Override
    public Optional<Customer> findById(final Long customerId) {
        return repository.findById(customerId);
    }

}
