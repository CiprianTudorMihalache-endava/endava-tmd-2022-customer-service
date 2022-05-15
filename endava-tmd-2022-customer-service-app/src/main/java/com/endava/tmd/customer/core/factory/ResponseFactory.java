package com.endava.tmd.customer.core.factory;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.swg.model.base.GenericResponse;
import com.endava.tmd.customer.swg.model.base.ParentResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponseFactory {
    private final BuildProperties buildProperties;

    /**
     * Builds a generic response with a single message
     * 
     * @param message The information to be passed to the caller
     * @return A generic response with the given message
     */
    public GenericResponse build(final String message) {
        return build(GenericResponse::new, message);
    }

    /**
     * Builds a generic response with multiple messages
     * 
     * @param messages The pieces of information to be passed to the caller
     * @return A generic response with the given messages
     */
    public GenericResponse build(final List<String> messages) {
        final var response = setDebugInfo(new GenericResponse());
        for (final String message : messages) {
            response.addAdditionalInfo(new AdditionalInfo().setMessage(message));
        }
        return response;
    }

    /**
     * Builds a specific response with a single message and a single result
     * 
     * @param <T>              The type of the response class
     * @param <R>              The type of the result class
     * @param responseSupplier The factory method that will be used to generate result objects
     * @param message          The information to be passed to the caller
     * @param result           The result object
     * @return A response with the specified type, containing the given message and the given result
     */
    public <T extends ParentResponse<T, R>, R> T build(final Supplier<T> responseSupplier,
                                                       final String message,
                                                       final R result) {
        return build(responseSupplier, message)
                .addResult(result);
    }

    private <T extends ParentResponse<T, R>, R> T build(final Supplier<T> responseSupplier,
                                                        final String message) {
        return setDebugInfo(responseSupplier.get())
                .addAdditionalInfo(new AdditionalInfo().setMessage(message));
    }

    private <T extends ParentResponse<T, R>, R> T setDebugInfo(final ParentResponse<T, R> response) {
        return response
                .setTraceId(MDC.get("traceId"))
                .setBuildVersion(buildProperties.getVersion());
    }

}
