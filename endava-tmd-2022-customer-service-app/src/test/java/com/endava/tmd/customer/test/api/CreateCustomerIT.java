package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.test.util.IntegrationTest;
import com.endava.tmd.customer.test.util.TestConstants;

@IntegrationTest
class CreateCustomerIT { // Note the name of the test class, it is not a standard surefire detected test class

    // MockMvc vs TestRestTemplate vs WebTestClient vs RestAssured (RestAssuredMockMvc | RestAssuredWebTestClient)
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void successfullyCreateACustomer() {
        final var request = new CreateCustomerRequest()
                .setFirstName("Peter")
                .setLastName("Pan");
        final var response = restTemplate.postForEntity("/v1/customers", request, CreateCustomerResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getAdditionalInfo()).singleElement()
                .extracting(AdditionalInfo::getMessage).isEqualTo("Customer created successfully");
        assertThat(response.getBody().getTraceId()).isNotBlank();
        assertThat(response.getBody().getBuildVersion()).isEqualTo(TestConstants.BUILD_VERSION);
    }

    @Test
    void failedValidationWhenCreatingACustomer() {
        final var request = new CreateCustomerRequest()
                .setLastName("Pan");
        final var response = restTemplate.postForEntity("/v1/customers", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).matches(
                """
                        \\{\
                        "timestamp":"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}",\
                        "status":400,\
                        "error":"Bad Request",\
                        "path":"\\/app-customer-service\\/v1\\/customers"\
                        \\}""");
        // How to report the problem to the caller?
        // Where does the client take the trace id?
    }

}
