package com.endava.tmd.customer;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Being always in UTC timezone will prevent a large number of future date&time issues
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
