package com.endava.tmd.customer.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.tmd.customer.core.converter.CustomerToRetrieveCustomerResultConverter;
import com.endava.tmd.customer.core.exception.RetrieveCustomerException;
import com.endava.tmd.customer.core.factory.ResponseFactory;
import com.endava.tmd.customer.core.port.in.RetrieveCustomerUseCase.RetrieveCustomerCommand;
import com.endava.tmd.customer.core.port.out.CustomerStorage;
import com.endava.tmd.customer.test.util.mother.model.CustomerMother;
import com.endava.tmd.customer.test.util.mother.swagger.RetrieveCustomerResponseMother;
import com.endava.tmd.customer.test.util.mother.swagger.RetrieveCustomerResultMother;

@ExtendWith(MockitoExtension.class)
class RetrieveCustomerServiceTest {
    private static final Long CUSTOMER_ID = 11L;

    @Mock
    private CustomerToRetrieveCustomerResultConverter modelToResultConverter;

    @Mock
    private CustomerStorage storage;

    @Mock
    private ResponseFactory responseFactory;

    @InjectMocks
    private RetrieveCustomerService service;

    @Test
    void customerFound() {
        final var customer = CustomerMother.peterPan().setId(CUSTOMER_ID);
        final var result = RetrieveCustomerResultMother.peterPan();
        final var response = RetrieveCustomerResponseMother.peterPan();

        when(storage.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(modelToResultConverter.convert(customer)).thenReturn(result);
        when(responseFactory.build(any(), eq(response.getAdditionalInfo().get(0).getMessage()), eq(response.getResults().get(0))))
                .thenReturn(response);

        final var actualResponse = service.retrieve(new RetrieveCustomerCommand(CUSTOMER_ID));
        assertThat(actualResponse).isSameAs(response);
    }

    @Test
    void customerNotFound() {
        final var command = new RetrieveCustomerCommand(CUSTOMER_ID);
        when(storage.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.retrieve(command))
                .isInstanceOf(RetrieveCustomerException.class)
                .hasMessage("Cannot find customer with id = " + CUSTOMER_ID);
        verifyNoInteractions(modelToResultConverter, responseFactory);
    }

}
