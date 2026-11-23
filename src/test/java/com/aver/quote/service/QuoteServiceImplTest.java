package com.aver.quote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.MockServerRestClientCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.aver.quote.domain.Quote;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

/**
 * Runs against the full application context so that the circuit breaker
 * annotation is really applied (it is silently skipped when spring-aop's
 * AspectJ support is missing from the classpath).
 */
@SpringBootTest
class QuoteServiceImplTest {

    private static final String UPSTREAM_URL = "https://api.chucknorris.io/jokes/random";
    private static final String UPSTREAM_JSON = "{\"id\":\"abc123\",\"value\":\"a real joke\",\"url\":\"ignored\"}";

    /**
     * Binds a mock server to the application's RestClient. It is a customizer without an order, so it is applied
     * after the one that sets the timeouts, and its mock request factory wins.
     */
    @TestConfiguration
    static class MockUpstreamConfig {

        @Bean
        MockServerRestClientCustomizer mockServerRestClientCustomizer() {
            return new MockServerRestClientCustomizer();
        }
    }

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private MockServerRestClientCustomizer mockServer;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private MockRestServiceServer upstream;

    @BeforeEach
    void setUp() {
        upstream = mockServer.getServer();
        upstream.reset();
        circuitBreakerRegistry.circuitBreaker("quoteService").reset();
    }

    @Test
    void returnsTheUpstreamJokeMappedToAQuote() {
        upstream.expect(requestTo(UPSTREAM_URL))
                .andRespond(withSuccess(UPSTREAM_JSON, MediaType.APPLICATION_JSON));

        Quote quote = quoteService.getRandomQuote();

        assertThat(quote.getType()).isEqualTo("success");
        assertThat(quote.getValue().getId()).isEqualTo("abc123");
        assertThat(quote.getValue().getJoke()).isEqualTo("a real joke");
        upstream.verify();
    }

    @Test
    void returnsTheFallbackWhenTheUpstreamFails() {
        upstream.expect(requestTo(UPSTREAM_URL)).andRespond(withServerError());

        Quote quote = quoteService.getRandomQuote();

        assertThat(quote.getValue().getId()).isEqualTo("1");
        upstream.verify();
    }

    @Test
    void returnsTheFallbackWhenTheConnectionFails() {
        upstream.expect(requestTo(UPSTREAM_URL)).andRespond(request -> {
            throw new IOException("connection refused");
        });

        Quote quote = quoteService.getRandomQuote();

        assertThat(quote.getValue().getId()).isEqualTo("1");
    }

    @Test
    void returnsTheFallbackWhenTheUpstreamBodyIsEmpty() {
        upstream.expect(requestTo(UPSTREAM_URL)).andRespond(withSuccess());

        Quote quote = quoteService.getRandomQuote();

        assertThat(quote.getValue().getId()).isEqualTo("1");
    }

    @Test
    void opensTheCircuitAfterRepeatedFailuresAndStopsCallingTheUpstream() {
        CircuitBreaker breaker = circuitBreakerRegistry.circuitBreaker("quoteService");

        // minimumNumberOfCalls is 5: five failures at a 100% failure rate open the circuit
        upstream.expect(times(5), requestTo(UPSTREAM_URL)).andRespond(withServerError());
        for (int i = 0; i < 5; i++) {
            quoteService.getRandomQuote();
        }
        assertThat(breaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        // the mock server accepts no further request, so reaching the upstream here would fail the test
        Quote quote = quoteService.getRandomQuote();

        assertThat(quote.getValue().getId()).isEqualTo("1");
        upstream.verify();
    }
}
