package com.endava.tmd.customer.core.port.out;

import com.endava.tmd.customer.model.Customer;

public interface CommunicationsApp {

    void reportNewCustomer(Customer customer);

}
