package com.endava.tmd.customer.core.exception;

import java.io.IOException;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import feign.Response;
import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;

public class ExternalServiceCallGenericException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExternalServiceCallGenericException(final String message) {
        super(message);
    }

    @FeignExceptionConstructor
    public ExternalServiceCallGenericException(@ResponseBody final Response response) {
        super("Client responded with " + response.status() + ", reason: " + response.reason() + ", body: "
                + extractResponseBody(response));
    }

    private static String extractResponseBody(final Response response) {
        try {
            final var body = response.body();
            if (body != null) {
                return IOUtils.toString(body.asInputStream(), StandardCharsets.UTF_8);
            }
            return "";
        } catch (final IOException e) {
            return "Unable to extract it: " + e.getMessage();
        }
    }

}
