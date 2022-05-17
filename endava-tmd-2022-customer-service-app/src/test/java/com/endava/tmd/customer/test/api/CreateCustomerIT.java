package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;
import com.endava.tmd.customer.test.util.mother.swagger.CreateCustomerRequestMother;

class CreateCustomerIT extends ApiIntegrationTest {

    @Test
    void successfullyCreateACustomer() {
        final var request = CreateCustomerRequestMother.peterPan();
        final var expectedResult = new CreateCustomerResult().setCustomerId(1L);

        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertResponse(response.getBody(), "Customer created successfully", expectedResult);
    }

    @Test
    void failedValidationWhenCreatingACustomer() {
        final var request = new CreateCustomerRequest()
                .setLastName("x".repeat(51));

        final var response = createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertResponse(response.getBody(), List.of("firstName must not be blank", "lastName size must be between 0 and 50",
                "dateOfBirth value must be older or equal than 18 years in the past"));
    }

    private ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        return getRestTemplate().postForEntity("/v1/customers", request, CreateCustomerResponse.class);
    }

}
