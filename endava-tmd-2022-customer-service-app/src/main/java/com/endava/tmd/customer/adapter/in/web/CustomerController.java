package com.endava.tmd.customer.adapter.in.web;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.endava.tmd.customer.swg.api.CustomerApi;
import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;
import com.endava.tmd.customer.swg.model.RetrieveCustomerResult;
import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.swg.model.base.ParentResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CustomerController implements CustomerApi {
    private final BuildProperties buildProperties;
    private List<CreateCustomerRequest> customers = new ArrayList<>();

    @Override
    public ResponseEntity<CreateCustomerResponse> createCustomer(final CreateCustomerRequest request) {
        log.info("Attempting to create customer: {}", request);
        customers.add(request);
        final var result = new CreateCustomerResult().setCustomerId((long) customers.size());
        final var response = buildResponse(CreateCustomerResponse::new, "Customer created successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RetrieveCustomerResponse> retrieveCustomer(final long customerId) {
        if (customerId > customers.size()) {
            // we cannot return a GenericResponse here, since GenericResponse does not extend RetrieveCustomerResponse
            final var response = buildResponse(RetrieveCustomerResponse::new, "Cannot find customer with id = " + customerId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        final var customer = customers.get((int) customerId - 1);
        final var result = new RetrieveCustomerResult()
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName());
        final var response = buildResponse(RetrieveCustomerResponse::new, "Retrieve operation was successfully processed",
                result);
        // this form is not so powerful
        // it cannot be used to return a 404 NOT FOUND with a body
        return ResponseEntity.ok(response);
    }

    private <T extends ParentResponse<T, R>, R> T buildResponse(final Supplier<T> responseSupplier,
                                                                final String message) {
        return setDebugInfo(responseSupplier.get())
                .addAdditionalInfo(new AdditionalInfo().setMessage(message));
    }

    private <T extends ParentResponse<T, R>, R> T buildResponse(final Supplier<T> responseSupplier,
                                                                final String message,
                                                                final R result) {
        return buildResponse(responseSupplier, message)
                .addResult(result);
    }

    private <T extends ParentResponse<T, R>, R> T setDebugInfo(final ParentResponse<T, R> response) {
        return response
                .setTraceId(MDC.get("traceId"))
                .setBuildVersion(buildProperties.getVersion());
    }

}
