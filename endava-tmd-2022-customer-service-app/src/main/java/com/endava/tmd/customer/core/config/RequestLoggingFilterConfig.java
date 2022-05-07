package com.endava.tmd.customer.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

// Inspired from https://www.baeldung.com/spring-http-logging
// Also, it is possible to define an interceptor that will do the logging
// Or... if you are really determined: https://stackoverflow.com/a/39137815/2032694

// Also, it is possible to read the logs from actuator, with an additional configuration: 
// https://stackoverflow.com/a/39234957/2032694
// https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints
@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        final var filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        // there are other customizations available, like customizing the before/after message prefix/suffix
        return filter;
    }
}
