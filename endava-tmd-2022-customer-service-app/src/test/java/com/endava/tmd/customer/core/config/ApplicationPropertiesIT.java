package com.endava.tmd.customer.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.endava.tmd.customer.test.util.IntegrationSliceTest;

@IntegrationSliceTest
class ApplicationPropertiesIT {
    // This does not work in our spring boot app: https://www.baeldung.com/spring-boot-testing-configurationproperties
    // The current approach is taken from: https://stackoverflow.com/a/70823870/2032694

    @Autowired
    private ApplicationProperties applicationProperties;

    @Configuration
    @EnableConfigurationProperties({ApplicationProperties.class})
    public static class Config {
    }

    @Test
    void testDefaultProperties() {
        assertThat(applicationProperties.getName()).isEqualTo("myApp");
        assertThat(applicationProperties.getDescription()).isEqualTo("The implementation of myApp");
        assertThat(applicationProperties.getLicense()).isEqualTo("myLicense");
        assertThat(applicationProperties.getLicenseUrl()).isEqualTo("https://myCompany.com/license");
        assertThat(applicationProperties.getContactName()).isEqualTo("myCompany");
        assertThat(applicationProperties.getContactUrl()).isEqualTo("https://www.myCompany.com");
        assertThat(applicationProperties.getContactMail()).isEqualTo("developer@myCompany.com");
    }
}
