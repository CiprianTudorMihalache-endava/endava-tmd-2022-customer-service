package com.endava.tmd.customer.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.tmd.customer.core.converter.CreateCustomerRequestToCustomerConverter;
import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.core.port.in.CreateCustomerUseCase.CreateCustomerCommand;
import com.endava.tmd.customer.core.port.out.CustomerStorage;
import com.endava.tmd.customer.test.util.mother.model.CustomerMother;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerRequestMother;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerResponseMother;

@ExtendWith(MockitoExtension.class)
class CreateCustomerServiceTest {

    @Mock
    private CreateCustomerRequestToCustomerConverter requestToModelConverter;

    @Mock
    private CustomerStorage storage;

    @Mock
    private ResponseFactory responseFactory;

    @InjectMocks
    private CreateCustomerService service;

    @Test
    void createCustomerSuccessfully() {
        final var customerId = 11L;
        final var request = CreateCustomerRequestMother.peterPan();
        final var customer = CustomerMother.peterPan();
        final var savedCustomer = CustomerMother.peterPan().setId(customerId);
        final var response = CreateCustomerResponseMother.create(customerId);

        when(requestToModelConverter.convert(request)).thenReturn(customer);
        when(storage.save(customer)).thenReturn(savedCustomer);
        when(responseFactory.build(any(), eq(response.getAdditionalInfo().get(0).getMessage()), eq(response.getResults().get(0))))
                .thenReturn(response);

        final var actualResponse = service.create(new CreateCustomerCommand(request));
        assertThat(actualResponse).isSameAs(response);
    }

}
