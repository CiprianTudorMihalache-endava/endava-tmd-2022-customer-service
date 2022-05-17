package com.endava.tmd.customer.core.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.endava.tmd.customer.test.util.mother.model.CustomerMother;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerRequestMother;
import com.github.dozermapper.core.DozerBeanMapperBuilder;

class CreateCustomerRequestToCustomerConverterTest {
    private final CreateCustomerRequestToCustomerConverter converter = new CreateCustomerRequestToCustomerConverter(
            DozerBeanMapperBuilder.buildDefault());

    @Test
    void convertOneObject() {
        final var request = CreateCustomerRequestMother.peterPan();
        final var expectedCustomer = CustomerMother.peterPan();
        assertThat(converter.convert(request)).isEqualTo(expectedCustomer);
    }

}
