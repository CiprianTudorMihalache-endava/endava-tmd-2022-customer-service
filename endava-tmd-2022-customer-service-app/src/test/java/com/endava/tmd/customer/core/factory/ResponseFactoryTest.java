package com.endava.tmd.customer.core.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.info.BuildProperties;

import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;
import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.swg.model.base.ParentResponse;

@ExtendWith(MockitoExtension.class)
class ResponseFactoryTest {
    private final static String BUILD_VERSION = "1.0.0-SNAPSHOT";

    @Mock
    private BuildProperties buildProperties;

    @InjectMocks
    private ResponseFactory responseFactory;

    @BeforeEach
    public void setup() {
        when(buildProperties.getVersion()).thenReturn(BUILD_VERSION);
    }

    @Test
    void genericResponseWithASingleMessage() {
        final var msg = "My message";
        final var response = responseFactory.build(msg);
        assertCorrectBuildVersionAndTraceId(response);
        assertThat(response.getAdditionalInfo()).singleElement()
                .extracting(AdditionalInfo::getMessage).isSameAs(msg);
        assertThat(response.getResults()).isEmpty();
    }

    @Test
    void genericResponseWithNoMessage() {
        final var response = responseFactory.build(List.of());
        assertCorrectBuildVersionAndTraceId(response);
        assertThat(response.getAdditionalInfo()).isEmpty();
        assertThat(response.getResults()).isEmpty();
    }

    @Test
    void genericResponseWithMultipleMessages() {
        final var msg1 = "My message 1";
        final var msg2 = "My message 2";
        final var response = responseFactory.build(List.of(msg1, msg2));
        assertCorrectBuildVersionAndTraceId(response);
        assertThat(response.getAdditionalInfo())
                .extracting(AdditionalInfo::getMessage)
                .containsExactly(msg1, msg2); // the messages will be delivered in order
        assertThat(response.getResults()).isEmpty();
    }

    @Test
    void specificResponseWithASingleResult() {
        final var msg = "My message";
        final var result = new CreateCustomerResult().setCustomerId(1L);

        final var response = responseFactory.build(CreateCustomerResponse::new, msg, result);

        assertCorrectBuildVersionAndTraceId(response);
        assertThat(response.getAdditionalInfo()).singleElement()
                .extracting(AdditionalInfo::getMessage).isSameAs(msg);
        assertThat(response.getResults()).singleElement().isSameAs(result);
    }

    private void assertCorrectBuildVersionAndTraceId(final ParentResponse<?, ?> response) {
        assertThat(response.getBuildVersion()).isSameAs(BUILD_VERSION);
        // unfortunately we are not in a web environment, thus we cannot test this
        assertThat(response.getTraceId()).isNull();
    }

}
