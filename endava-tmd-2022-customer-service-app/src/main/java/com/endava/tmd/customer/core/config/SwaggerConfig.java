package com.endava.tmd.customer.core.config;

import static org.springdoc.core.Constants.ALL_PATTERN;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    // What library should we choose?
    // * Springdoc: https://github.com/springdoc/springdoc-openapi
    // * Springfox: https://github.com/springfox/springfox
    // Unfortunately Springfox is not developed anymore and it does not work with the latest spring boot
    // https://stackoverflow.com/questions/70036953/springboot-2-6-0-spring-fox-3-failed-to-start-bean-documentationpluginsboot

    // Swagger page: http://localhost:9010/app-customer-service/swagger-ui/index.html

    private final ApplicationProperties appProperties;
    private final BuildProperties buildProperties;

    @Bean
    public OpenAPI customerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(appProperties.getName())
                        .description(appProperties.getDescription())
                        .version("v" + buildProperties.getVersion())
                        .license(new License()
                                .name(appProperties.getLicense())
                                .url(appProperties.getLicenseUrl()))
                        .contact(new Contact()
                                .name(appProperties.getContactName())
                                .email(appProperties.getContactMail())
                                .url(appProperties.getContactUrl())));
    }

    @Bean
    @Profile("!prod")
    public GroupedOpenApi actuatorApi(final OpenApiCustomiser actuatorOpenApiCustomiser,
                                      final OperationCustomizer actuatorCustomizer,
                                      final WebEndpointProperties endpointProperties) {
        return GroupedOpenApi.builder()
                .group("Actuator")
                .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
                .addOpenApiCustomiser(actuatorOpenApiCustomiser)
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Actuator API")))
                .addOperationCustomizer(actuatorCustomizer)
                .pathsToExclude("/health/*")
                .build();
    }

}
