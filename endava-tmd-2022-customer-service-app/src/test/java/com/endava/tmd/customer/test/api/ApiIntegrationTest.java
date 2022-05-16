package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.swg.model.base.ParentResponse;
import com.endava.tmd.customer.test.util.IntegrationWebTest;
import com.endava.tmd.customer.test.util.TestConstants;

import lombok.Getter;

@Getter
@IntegrationWebTest
abstract class ApiIntegrationTest {

    // These test executions could be documented using spring-restdocs library
    // https://docs.spring.io/spring-restdocs/docs/current/reference/html5/
    // Difference vs springdoc/OpenAPI: https://springframework.guru/should-i-use-spring-rest-docs-or-openapi/
    // Other example: https://www.baeldung.com/spring-rest-docs

    @Autowired
    private TestRestTemplate restTemplate;

    protected <T extends ParentResponse<T, R>, R> void assertResponse(final T response, final String expectedMessage) {
        assertResponse(response, List.of(expectedMessage));
    }

    protected <T extends ParentResponse<T, R>, R> void assertResponse(final T response, final List<String> expectedMessages) {
        assertResponse(response, expectedMessages, List.of());
    }

    protected <T extends ParentResponse<T, R>, R> void assertResponse(final T response, final String expectedMessage,
                                                                      final R expectedResult) {
        assertResponse(response, List.of(expectedMessage), List.of(expectedResult));
    }

    protected <T extends ParentResponse<T, R>, R> void assertResponse(final T response, final List<String> expectedMessages,
                                                                      final List<R> expectedResults) {
        assertThat(response.getBuildVersion()).isEqualTo(TestConstants.BUILD_VERSION);
        assertThat(response.getTraceId()).isNotBlank();
        assertThat(response.getAdditionalInfo()).extracting(AdditionalInfo::getMessage)
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
        assertThat(response.getResults()).containsExactlyInAnyOrderElementsOf(expectedResults);
    }

}
