package com.endava.tmd.customer.test.util.mother.swagger;

import com.endava.tmd.customer.swg.model.RetrieveCustomerResponse;
import com.endava.tmd.customer.swg.model.base.AdditionalInfo;
import com.endava.tmd.customer.test.util.TestConstants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetrieveCustomerResponseMother {

    public static RetrieveCustomerResponse jamesBond() {
        return new RetrieveCustomerResponse()
                .setBuildVersion(TestConstants.BUILD_VERSION)
                .addAdditionalInfo(new AdditionalInfo().setMessage("Retrieve operation was successfully processed"))
                .addResult(RetrieveCustomerResultMother.jamesBond());
    }

}
