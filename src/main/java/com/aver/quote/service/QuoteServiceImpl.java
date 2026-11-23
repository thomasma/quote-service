package com.aver.quote.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aver.quote.config.QuoteProperties;
import com.aver.quote.domain.Joke;
import com.aver.quote.domain.Quote;
import com.aver.quote.domain.UpstreamJoke;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuoteServiceImpl implements QuoteService {

    private final QuoteProperties appProperties;

    private final RestTemplate restTemplate;

    public QuoteServiceImpl(RestTemplate rest, QuoteProperties appProperties) {
        this.restTemplate = rest;
        this.appProperties = appProperties;
    }

    /**
     * Call an external service to retrieve a random joke.
     *
     * @return a random quote
     */
    @CircuitBreaker(name = "quoteService", fallbackMethod = "fallback")
    public Quote getRandomQuote() {
        log.info("Getting a random quote");

        String svcURL = appProperties.getServiceUri() + appProperties.getServicePath();
        UpstreamJoke joke = restTemplate.getForObject(svcURL, UpstreamJoke.class);
        if (joke == null) {
            // counted as a failure by the circuit breaker, and served by the fallback
            throw new RestClientException("Empty response from the quote service");
        }

        Joke value = new Joke();
        value.setId(joke.getId());
        value.setJoke(joke.getValue());
        Quote quote = new Quote();
        quote.setType("success");
        quote.setValue(value);
        return quote;
    }

    /**
     * Fallback method in case there is an exception getting a joke from the
     * remote service
     * 
     * @param e
     * @return
     */
    public Quote fallback(Exception e) {
        // no stack trace: while the circuit is open this runs on every request
        log.warn("Quote service call failed, returning the fallback quote: {}", e.toString());
        Quote q = new Quote();
        q.setType("success");
        Joke v = new Joke();
        v.setId("1");
        v.setJoke("Chuck Norris is right now sleeping, but he can see you with both eyes closed...so behave!");
        q.setValue(v);
        return q;
    }
}
