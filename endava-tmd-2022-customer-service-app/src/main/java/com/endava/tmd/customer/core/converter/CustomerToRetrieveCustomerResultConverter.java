package com.endava.tmd.customer.core.converter;

import org.springframework.stereotype.Component;

import com.endava.tmd.customer.model.Customer;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResult;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerToRetrieveCustomerResultConverter implements Converter<Customer, RetrieveCustomerResult> {
    private final Mapper mapper;

    @Override
    public RetrieveCustomerResult convert(final Customer source) {
        return mapper.map(source, RetrieveCustomerResult.class);
    }

}
