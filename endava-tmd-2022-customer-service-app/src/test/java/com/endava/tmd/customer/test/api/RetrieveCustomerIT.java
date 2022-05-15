package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;

class RetrieveCustomerIT extends ApiIntegrationTest {

    @Test
    void retrieveInexistentCustomer() {
        final var response = retrieveCustomer(10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertResponse(response.getBody(), "Cannot find customer with id = 10");
    }

    @Test
    void failedValidationWhenRetrievingACustomer() {
        final var response = retrieveCustomer(-1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertResponse(response.getBody(), "customerId fails constraint validation: must be greater than or equal to 1");
    }

    private ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(final long customerId) {
        return getRestTemplate().getForEntity("/v1/customers/{id}", RetrieveCustomerResponse.class, customerId);
    }

}
