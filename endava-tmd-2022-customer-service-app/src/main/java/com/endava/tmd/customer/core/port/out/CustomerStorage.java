package com.endava.tmd.customer.core.port.out;

import java.util.Optional;

import com.endava.tmd.customer.model.Customer;

public interface CustomerStorage {

    Customer saveAndFlush(Customer customer);

    Optional<Customer> findById(Long customerId);

}
