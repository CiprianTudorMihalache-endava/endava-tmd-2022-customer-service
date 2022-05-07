package com.endava.tmd.customer.swg.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.endava.tmd.customer.swg.model.CreateCustomerRequest;
import com.endava.tmd.customer.swg.model.CreateCustomerResponse;

public interface CustomerApi {

    @PostMapping(value = "/v1/customers", produces = {"application/json"})
    ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request);
    // We decided to use ResponseEntity, since it gives you great flexibility in building the response
    // See: https://www.baeldung.com/spring-response-entity
    // An alternative would be to return directly the DTO and use @ResponseBody instead
    // See: https://www.baeldung.com/spring-request-response-body

}
