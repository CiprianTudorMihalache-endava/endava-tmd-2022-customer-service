package com.endava.tmd.customer.core.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
@Slf4j
public class ApplicationProperties {
    /** Application name */
    private String name;

    /** Application description */
    private String description;

    /** Application license */
    private String license;

    /** URL location of the application license */
    private String licenseUrl;

    /** Name of the organization that develops the application */
    private String contactName;

    /** URL of the organization that develops the application */
    private String contactUrl;

    /** Mail address of the organization that develops the application */
    private String contactMail;

    @PostConstruct
    public void writeConfigurationToLog() {
        log.info("Starting application by using the application properties: {}", this);
    }
}
