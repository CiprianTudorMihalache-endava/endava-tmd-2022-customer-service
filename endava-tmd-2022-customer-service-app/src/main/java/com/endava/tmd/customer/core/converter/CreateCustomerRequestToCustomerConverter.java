package com.endava.tmd.customer.core.converter;

import org.springframework.stereotype.Component;

import com.endava.tmd.customer.model.Customer;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// If you are not familiar with Dozer you could look at these resources first:
// * https://simplesolution.dev/using-dozer-spring-boot-starter/
// * https://www.baeldung.com/dozer
// We decided to go with a dedicated converter class instead of the dozer mapping file, since this way it is easier to test
public class CreateCustomerRequestToCustomerConverter implements Converter<CreateCustomerRequest, Customer> {
    private final Mapper mapper;

    @Override
    public Customer convert(final CreateCustomerRequest source) {
        return mapper.map(source, Customer.class);
    }

}
