package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;
import com.endava.tmd.customer.test.util.IntegrationTest;
import com.endava.tmd.customer.test.util.TestConstants;

@IntegrationTest
class RetrieveCustomerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void retrieveInexistentCustomer() {
        final var response = restTemplate.getForEntity("/v1/customers/10", RetrieveCustomerResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Cannot find customer with id = 10");
        assertThat(response.getBody().getTraceId()).isNotBlank();
        assertThat(response.getBody().getBuildVersion()).isEqualTo(TestConstants.BUILD_VERSION);
    }

    @Test
    void failedValidationWhenRetrievingACustomer() {
        final var response = restTemplate.getForEntity("/v1/customers/0", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR); // !!!
        assertThat(response.getBody()).matches(
                """
                        \\{\
                        "timestamp":"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}",\
                        "status":500,\
                        "error":"Internal Server Error",\
                        "path":"\\/app-customer-service\\/v1\\/customers\\/0"\
                        \\}""");
        // How to report the problem to the caller?
        // Where does the client take the trace id?
    }

}
