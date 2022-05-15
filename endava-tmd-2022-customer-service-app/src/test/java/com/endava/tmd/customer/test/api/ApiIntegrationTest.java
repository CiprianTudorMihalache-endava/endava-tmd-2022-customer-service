package com.endava.tmd.customer.test.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.swg.model.base.ParentResponse;
import com.endava.tmd.customer.test.util.IntegrationTest;
import com.endava.tmd.customer.test.util.TestConstants;

import lombok.Getter;

@Getter
@IntegrationTest
abstract class ApiIntegrationTest {

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
