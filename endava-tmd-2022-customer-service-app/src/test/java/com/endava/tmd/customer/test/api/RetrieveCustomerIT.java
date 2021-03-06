package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import com.endava.tmd.customer.adapter.out.db.CustomerRepository;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;
import com.endava.tmd.customer.test.util.mother.swagger.RetrieveCustomerResultMother;

class RetrieveCustomerIT extends ApiIntegrationTest {

    @SpyBean
    private CustomerRepository customerRepositorySpy;

    @Test
    void retrieveExistentCustomer() {
        final var expectedResult = RetrieveCustomerResultMother.jamesBond();
        final var response = retrieveCustomer(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertResponse(response.getBody(), "Retrieve operation was successfully processed", expectedResult);
        final var results = response.getBody().getResults();
        assertThat(results).singleElement().satisfies(result -> {
            assertThat(result.getCreateDateTime()).isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.SECONDS));
            assertThat(result.getLastUpdateDateTime()).isCloseTo(result.getCreateDateTime(), within(1, ChronoUnit.MILLIS));
        });
    }

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

    @Test
    void dbFailureWhenTryingToRetrieveACustomer() throws Exception {
        final var msg = "Mock exception message";

        doThrow(new DataSourceLookupFailureException(msg)).when(customerRepositorySpy).findById(1L);

        final var response = retrieveCustomer(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FAILED_DEPENDENCY);
        assertResponse(response.getBody(), msg);

        reset(customerRepositorySpy);
    }

    private ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(final long customerId) {
        return getRestTemplate().getForEntity("/v1/customers/{id}", RetrieveCustomerResponse.class, customerId);
    }

}
