package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import com.endava.tmd.customer.adapter.out.communications.CommunicationsAppClient;
import com.endava.tmd.customer.adapter.out.communications.model.NewCustomer;
import com.endava.tmd.customer.adapter.out.db.CustomerRepository;
import com.endava.tmd.customer.core.exception.ExternalServiceCallBadRequestException;
import com.endava.tmd.customer.core.exception.ExternalServiceCallGenericException;
import com.endava.tmd.customer.model.Customer;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;
import com.endava.tmd.customer.test.util.TestConstants;
import com.endava.tmd.customer.test.util.mother.model.CustomerMother;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerRequestMother;

class CreateCustomerIT extends ApiIntegrationTest {

    // This is one approach
    // Another approach would be to avoid any mocking, but use WireMock in this test, similar to CommunicationsAppClientIT
    @MockBean
    private CommunicationsAppClient communicationsAppClient;

    @SpyBean
    private CustomerRepository customerRepositorySpy;

    @Test
    void successfullyCreateACustomer() {
        final var newCustomerId = TestConstants.INITIAL_DB_RECORDS + 1;
        final var request = CreateCustomerRequestMother.peterPan();
        final var expectedResult = new CreateCustomerResult().setCustomerId(newCustomerId);

        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertResponse(response.getBody(), "Customer created successfully", expectedResult);
        final var savedEntity = getCustomerEntity(newCustomerId);
        assertThat(savedEntity).usingRecursiveComparison().ignoringFieldsOfTypes(OffsetDateTime.class)
                .isEqualTo(CustomerMother.peterPanPersisted(newCustomerId, 0L));
        assertThat(savedEntity.getCreateDateTime()).isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.SECONDS));
        assertThat(savedEntity.getLastUpdateDateTime()).isCloseTo(savedEntity.getCreateDateTime(), within(1, ChronoUnit.MILLIS));
        verify(communicationsAppClient).reportNewCustomer(expectedCommModel(request));
    }

    @Test
    void failedValidationWhenCreatingACustomer() {
        final var request = new CreateCustomerRequest()
                .setLastName("x".repeat(51));

        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertResponse(response.getBody(), List.of("firstName must not be blank", "lastName size must be between 0 and 50",
                "dateOfBirth value must be older or equal than 18 years in the past"));
        verifyNoInteractions(communicationsAppClient);
        verifyNoCustomerWasCreated();
    }

    @Test
    void dbFailureWhenTryingToCreateACustomer() throws Exception {
        final var msg = "Mock exception message";
        final var request = CreateCustomerRequestMother.peterPan();

        doThrow(new DataSourceLookupFailureException(msg)).when(customerRepositorySpy).saveAndFlush(any(Customer.class));

        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FAILED_DEPENDENCY);
        assertResponse(response.getBody(), msg);
        verifyNoInteractions(communicationsAppClient);
        verifyNoCustomerWasCreated();

        reset(customerRepositorySpy);
    }

    @Test
    void failedValidationResponseOnCommunicationsAppWhenCreatingACustomer() {
        final var msg = "Mock exception message";
        final var request = CreateCustomerRequestMother.peterPan();

        doThrow(new ExternalServiceCallBadRequestException(msg))
                .when(communicationsAppClient).reportNewCustomer(expectedCommModel(request));
        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertResponse(response.getBody(), msg);
        // the transaction should have been rolled back
        verifyNoCustomerWasCreated();
    }

    @Test
    void exceptionalResponseOnCommunicationsAppWhenCreatingACustomer() {
        final var msg = "Mock exception message";
        final var request = CreateCustomerRequestMother.peterPan();

        doThrow(new ExternalServiceCallGenericException(msg))
                .when(communicationsAppClient).reportNewCustomer(expectedCommModel(request));
        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FAILED_DEPENDENCY);
        assertResponse(response.getBody(), msg);
        // the transaction should have been rolled back
        verifyNoCustomerWasCreated();
    }

    private ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        return getRestTemplate().postForEntity("/v1/customers", request, CreateCustomerResponse.class);
    }

    private NewCustomer expectedCommModel(final CreateCustomerRequest request) {
        return new NewCustomer()
                .setCustomerId(TestConstants.INITIAL_DB_RECORDS + 1)
                .setFirstName(request.getFirstName())
                .setDateOfBirth(request.getDateOfBirth());
    }

    private void verifyNoCustomerWasCreated() {
        assertThat(getCustomerEntity(TestConstants.INITIAL_DB_RECORDS + 1)).isNull();
    }

}
