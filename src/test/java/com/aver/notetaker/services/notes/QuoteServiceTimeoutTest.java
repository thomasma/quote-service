package com.aver.notetaker.services.notes;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.aver.notetaker.domain.Quote;
import com.sun.net.httpserver.HttpServer;

/**
 * Uses a real, deliberately slow HTTP server to prove that the RestTemplate
 * read timeout is applied: without it the call would block for the full 5 seconds.
 */
@SpringBootTest(properties = { "quote.read-timeout=300ms", "quote.connect-timeout=300ms" })
class QuoteServiceTimeoutTest {

    private static final long UPSTREAM_DELAY_MS = 5000;

    private static final HttpServer SLOW_UPSTREAM = startSlowUpstream();

    @DynamicPropertySource
    static void upstreamProperties(DynamicPropertyRegistry registry) {
        registry.add("quote.service-uri", () -> "http://localhost:" + SLOW_UPSTREAM.getAddress().getPort());
        registry.add("quote.service-path", () -> "/jokes/random");
    }

    @Autowired
    private QuoteService quoteService;

    @AfterAll
    static void stopUpstream() {
        SLOW_UPSTREAM.stop(0);
    }

    @Test
    void givesUpOnASlowUpstreamAndReturnsTheFallback() {
        long start = System.nanoTime();

        Quote quote = quoteService.getRandomQuote();

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertThat(quote.getValue().getId()).isEqualTo("1");
        assertThat(elapsedMs).isLessThan(UPSTREAM_DELAY_MS / 2);
    }

    private static HttpServer startSlowUpstream() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            server.createContext("/", exchange -> {
                try {
                    Thread.sleep(UPSTREAM_DELAY_MS);
                    exchange.sendResponseHeaders(200, -1);
                } catch (InterruptedException | IOException e) {
                    // the client gave up, which is what the test expects
                } finally {
                    exchange.close();
                }
            });
            server.start();
            return server;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
