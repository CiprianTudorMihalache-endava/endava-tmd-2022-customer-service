package com.endava.tmd.customer.core.exception;

import java.io.Serial;

public class RetrieveCustomerException extends RuntimeException {
    @Serial // not mandatory but useful (https://stackoverflow.com/a/63783607/2032694)
    private static final long serialVersionUID = 1L;

    public RetrieveCustomerException(final Long customerId) {
        super("Cannot find customer with id = " + customerId);
    }

}
