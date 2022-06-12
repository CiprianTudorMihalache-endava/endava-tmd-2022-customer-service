package com.endava.tmd.customer.core.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

import com.endava.tmd.customer.test.util.mother.model.CustomerMother;
import com.endava.tmd.customer.test.util.mother.swagger.RetrieveCustomerResultMother;
import com.github.dozermapper.core.DozerBeanMapperBuilder;

class CustomerToRetrieveCustomerResultConverterTest {
    private final CustomerToRetrieveCustomerResultConverter converter = new CustomerToRetrieveCustomerResultConverter(
            DozerBeanMapperBuilder.buildDefault());

    @Test
    void convertOneObject() {
        final var creationDate = OffsetDateTime.now().minus(Duration.ofMinutes(1));
        final var lastUpdateDate = OffsetDateTime.now();
        final var customer = CustomerMother.peterPanPersisted(123L, 7L)
                .setCreateDateTime(creationDate)
                .setLastUpdateDateTime(lastUpdateDate);
        final var expectedResponse = RetrieveCustomerResultMother.peterPan(123L, 7L)
                .setCreateDateTime(OffsetDateTime.from(creationDate)) // avoid using the same object
                .setLastUpdateDateTime(OffsetDateTime.from(lastUpdateDate));
        assertThat(converter.convert(customer)).isEqualTo(expectedResponse);
    }

}
