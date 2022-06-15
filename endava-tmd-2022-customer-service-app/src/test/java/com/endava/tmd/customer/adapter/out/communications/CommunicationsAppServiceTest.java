package com.endava.tmd.customer.adapter.out.communications;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.tmd.customer.adapter.out.communications.model.NewCustomer;
import com.endava.tmd.customer.test.util.mother.model.CustomerMother;

@ExtendWith(MockitoExtension.class)
class CommunicationsAppServiceTest {

    @Mock
    private CommunicationsAppClient appClient;

    @InjectMocks
    private CommunicationsAppService service;

    @Test
    void reportNewCustomer() {
        final var customer = CustomerMother.jamesBond();
        final var expectedCommModel = new NewCustomer() // another option would be to capture it
                .setCustomerId(customer.getId())
                .setFirstName(customer.getFirstName())
                .setDateOfBirth(customer.getDateOfBirth());

        service.reportNewCustomer(customer);

        verify(appClient).reportNewCustomer(expectedCommModel);
    }

}
