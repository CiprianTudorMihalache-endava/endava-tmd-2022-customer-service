package com.endava.tmd.customer.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.endava.tmd.customer.core.port.in.CreateCustomerUseCase;
import com.endava.tmd.customer.core.port.in.RetrieveCustomerUseCase;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerRequestMother;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerResponseMother;
import com.endava.tmd.customer.test.util.mother.swagger.RetrieveCustomerResponseMother;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CreateCustomerUseCase createCustomerUseCase;

    @Mock
    private RetrieveCustomerUseCase retrieveCustomerUseCase;

    @InjectMocks
    private CustomerController controller;

    @Test
    void successfullyCreateCustomer() {
        final var request = CreateCustomerRequestMother.peterPan();
        final var command = new CreateCustomerUseCase.CreateCustomerCommand(request);
        final var response = CreateCustomerResponseMother.create(1L);

        when(createCustomerUseCase.create(command)).thenReturn(response);

        final var responseEntity = controller.createCustomer(request);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isSameAs(response);
    }

    @Test
    void successfullyRetrieveCustomer() {
        final var customerId = 123L;
        final var command = new RetrieveCustomerUseCase.RetrieveCustomerCommand(customerId);
        final var response = RetrieveCustomerResponseMother.jamesBond();

        when(retrieveCustomerUseCase.retrieve(command)).thenReturn(response);

        final var responseEntity = controller.retrieveCustomer(customerId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isSameAs(response);
    }

}
