package com.endava.tmd.customer.core.exception;

import java.io.Serial;

import feign.error.FeignExceptionConstructor;

public class ExternalServiceCallBadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    @FeignExceptionConstructor
    // Use the actual object instead of String, if it is the case
    // Or even more: https://github.com/OpenFeign/feign-annotation-error-decoder
    public ExternalServiceCallBadRequestException(final String message) {
        super(message);
    }

}
