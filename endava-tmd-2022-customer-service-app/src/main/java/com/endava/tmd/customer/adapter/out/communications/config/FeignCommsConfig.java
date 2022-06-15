package com.endava.tmd.customer.adapter.out.communications.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.endava.tmd.customer.adapter.out.communications.CommunicationsAppClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.error.AnnotationErrorDecoder;

public class FeignCommsConfig {

    @Bean
    public RequestInterceptor eisBasicAuthRequestInterceptor(final @Value("${customer.client.comms-service.basic-authentication.username}") String username,
                                                             final @Value("${customer.client.comms-service.basic-authentication.password}") String password) {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder eisFeignAnnotationErrorDecoder(final ObjectMapper objectMapper) {
        return AnnotationErrorDecoder.builderFor(CommunicationsAppClient.class)
                // this can be customized as needed. For instance, in case the response is a json, we could use
                // .withResponseBodyDecoder(new JacksonDecoder(objectMapper))
                // see: https://github.com/OpenFeign/feign-annotation-error-decoder
                .build();
    }
}
