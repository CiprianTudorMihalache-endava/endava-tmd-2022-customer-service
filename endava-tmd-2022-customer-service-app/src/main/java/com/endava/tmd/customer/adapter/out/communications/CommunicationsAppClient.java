package com.endava.tmd.customer.adapter.out.communications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.endava.tmd.customer.adapter.out.communications.config.FeignCommsConfig;
import com.endava.tmd.customer.adapter.out.communications.model.NewCustomer;
import com.endava.tmd.customer.core.exception.ExternalServiceCallBadRequestException;
import com.endava.tmd.customer.core.exception.ExternalServiceCallGenericException;

import feign.error.ErrorCodes;
import feign.error.ErrorHandling;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@CircuitBreaker(name = "comms-client-circuit-breaker")
@Retry(name = "comms-client-retry")
@ErrorHandling(codeSpecific = @ErrorCodes(codes = {400, 404}, generate = ExternalServiceCallBadRequestException.class),
        defaultException = ExternalServiceCallGenericException.class)
@FeignClient(name = "comms-client", url = "${customer.client.comms-service.host}",
        path = "${customer.client.comms-service.path}", primary = false, configuration = FeignCommsConfig.class)
public interface CommunicationsAppClient {

    @ErrorHandling
    @PostMapping(path = "${customer.client.comms-service.endpoint.report-new-customer}")
    void reportNewCustomer(@RequestBody final NewCustomer newCustomer);

}
