/**
 * 
 */
package com.endava.tmd.customer.test.util.mother.swagger;

import com.endava.tmd.customer.swg.model.CreateCustomerResponse;
import com.endava.tmd.customer.swg.model.CreateCustomerResult;
import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.test.util.TestConstants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateCustomerResponseMother {

    public static CreateCustomerResponse create(final long customerId) {
        return new CreateCustomerResponse()
                .setBuildVersion(TestConstants.BUILD_VERSION)
                .addAdditionalInfo(new AdditionalInfo().setMessage("Customer created successfully"))
                .addResult(new CreateCustomerResult().setCustomerId(customerId));
    }

}
