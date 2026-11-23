package com.aver.quote;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import io.micrometer.tracing.Tracer;

/**
 * The trace and span ids in the logs depend on Boot's tracing auto-configuration, which lives in its own module
 * and silently switches off when that module is missing from the classpath.
 */
@SpringBootTest
class TracingConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void tracingIsAutoConfigured() {
        assertThat(context.getBeansOfType(Tracer.class)).isNotEmpty();
    }
}
