package com.endava.tmd.customer.test.util;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestExecutionLogger implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        final var testMethod = context.getRequiredTestMethod();
        log.info("{}#{} started\n", context.getRequiredTestClass().getName(), testMethod.getName());
        getStore(context).put(testMethod, System.nanoTime());
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        final var testMethod = context.getRequiredTestMethod();
        final long start = getStore(context).remove(testMethod, long.class);
        final var duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        final var executionException = context.getExecutionException();
        if (executionException.isPresent()) {
            log.info("#{} failed after {} ms: {}", testMethod.getName(), duration, executionException.get());
        } else {
            log.info("#{} succeeded after {} ms\n\n", testMethod.getName(), duration);
        }
    }

    private Store getStore(final ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context));
    }

}
