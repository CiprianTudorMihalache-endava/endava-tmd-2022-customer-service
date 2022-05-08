package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.test.util.TestConstants;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateCustomerIT { // Note the name of the test class, it is not a standard surefire detected test class

    // MockMvc vs TestRestTemplate vs WebTestClient vs RestAssured (RestAssuredMockMvc | RestAssuredWebTestClient)
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void successfullyCreateACustomer() {
        final var request = new CreateCustomerRequest()
                .setFirstName("Pater")
                .setLastName("Pan");
        final var response = restTemplate.postForEntity("/v1/customers", request, CreateCustomerResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getMessage()).isEqualTo("Customer created successfully");
        assertThat(response.getBody().getTraceId()).isNotBlank();
        assertThat(response.getBody().getBuildVersion()).isEqualTo(TestConstants.BUILD_VERSION);
    }
}
