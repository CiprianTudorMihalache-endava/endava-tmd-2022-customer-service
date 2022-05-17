package com.endava.tmd.customer.core.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.endava.tmd.customer.test.util.mother.model.CustomerMother;
import com.endava.tmd.customer.test.util.mother.swagger.RetrieveCustomerResultMother;
import com.github.dozermapper.core.DozerBeanMapperBuilder;

class CustomerToRetrieveCustomerResultConverterTest {
    private final CustomerToRetrieveCustomerResultConverter converter = new CustomerToRetrieveCustomerResultConverter(
            DozerBeanMapperBuilder.buildDefault());

    @Test
    void convertOneObject() {
        final var customer = CustomerMother.peterPan();
        final var expectedResponse = RetrieveCustomerResultMother.peterPan();
        assertThat(converter.convert(customer)).isEqualTo(expectedResponse);
    }

}
