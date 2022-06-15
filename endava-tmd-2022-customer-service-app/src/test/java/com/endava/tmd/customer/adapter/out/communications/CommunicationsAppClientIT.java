package com.endava.tmd.customer.adapter.out.communications;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import com.endava.tmd.customer.adapter.out.communications.model.NewCustomer;
import com.endava.tmd.customer.adapter.out.db.CustomerRepository;
import com.endava.tmd.customer.core.exception.ExternalServiceCallBadRequestException;
import com.endava.tmd.customer.core.exception.ExternalServiceCallGenericException;
import com.endava.tmd.customer.test.util.IntegrationSliceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class, DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@IntegrationSliceTest
@AutoConfigureWireMock(port = 0)
class CommunicationsAppClientIT {
    private static final String REPORT_URL = "/communications-service/v1/report-new-customer";
    private static final NewCustomer NEW_CUSTOMER = new NewCustomer();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    // we do not need it, but we need to mock it since its dependencies are not available (see the exclusions above)
    private CustomerRepository customerRepository;

    @Autowired
    private CommunicationsAppClient client;

    @Value("${customer.client.comms-service.basic-authentication.username}")
    private String username;

    @Value("${customer.client.comms-service.basic-authentication.password}")
    private String password;

    @Test
    void successfulResponse() throws JsonProcessingException {
        stubFor(post(urlPathEqualTo(REPORT_URL))
                .withBasicAuth(username, password)
                .willReturn(okJson(OBJECT_MAPPER
                        .writeValueAsString(new CommunicationsSuccessResponse().setMessage("Operation was successful")))));

        assertThatCode(() -> client.reportNewCustomer(NEW_CUSTOMER))
                .doesNotThrowAnyException();
    }

    @Test
    void badRequestResponse() throws JsonProcessingException {
        final var errorMsg = "Missing attribute: customerID";
        stubFor(post(urlPathEqualTo(REPORT_URL))
                .withBasicAuth(username, password)
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody(errorMsg)));

        assertThatThrownBy(() -> client.reportNewCustomer(NEW_CUSTOMER))
                .isInstanceOf(ExternalServiceCallBadRequestException.class)
                .hasMessage(errorMsg);
    }

    @Test
    void internalServerErrorResponse() throws JsonProcessingException {
        final var errorMsg = "Unexpected processing error";
        stubFor(post(urlPathEqualTo(REPORT_URL))
                .withBasicAuth(username, password)
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody(errorMsg)));

        assertThatThrownBy(() -> client.reportNewCustomer(NEW_CUSTOMER))
                .isInstanceOf(ExternalServiceCallGenericException.class)
                .hasMessage("Client responded with 500, reason: Server Error, body: " + errorMsg);
    }

    @Data
    private static class CommunicationsSuccessResponse {
        // in the production code we do not care about the response from the communication application
        // but, this is here in order to show an example on how we would construct a desired response
        private String message;
    }
}
