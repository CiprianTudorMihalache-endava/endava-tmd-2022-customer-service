package com.endava.tmd.customer.adapter.out.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.tmd.customer.test.util.mother.model.CustomerMother;

@ExtendWith(MockitoExtension.class)
class CustomerDbStorageServiceTest {

    private static final long TEST_ID = 123L;

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerDbStorageService service;

    @Test
    void shouldSaveCustomer() {
        final var initialCustomer = CustomerMother.peterPan();
        final var savedCustomer = CustomerMother.peterPanPersisted(12L, 0L);

        when(repository.saveAndFlush(initialCustomer)).thenReturn(savedCustomer);

        assertThat(service.saveAndFlush(initialCustomer)).isSameAs(savedCustomer);
    }

    @Test
    void shouldRetrieveExistingCustomer() {
        final var savedCustomer = CustomerMother.peterPanPersisted(TEST_ID, 0L);
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(savedCustomer));
        assertThat(service.findById(TEST_ID)).hasValue(savedCustomer);
    }

    @Test
    void shouldReturnEmptyObjectWhenCustomerDoesNotExist() {
        when(repository.findById(TEST_ID)).thenReturn(Optional.empty());
        assertThat(service.findById(TEST_ID)).isEmpty();
    }

}
