package com.aver.notetaker.services.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aver.notetaker.domain.Quote;
import com.aver.notetaker.domain.Value;
import com.aver.util.ApplicationProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private ApplicationProperties appProperties;

    private final RestTemplate restTemplate;

    public QuoteServiceImpl(RestTemplate rest) {
        this.restTemplate = rest;
    }

    /**
     * Call an external service to retrieve a random joke.
     *
     * @return a random quote
     */
    @HystrixCommand(fallbackMethod = "fallback")
    public Quote getRandomQuote() {
        log.info("Getting a random quote");

        StringBuffer svcURL = new StringBuffer();
        svcURL.append(appProperties.getQuoteSvcURI());
        svcURL.append(appProperties.getQuoteSvcPath());
        Quote quote = restTemplate.getForObject(svcURL.toString(), Quote.class);
        return quote;
    }

    public Quote fallback() {
        Quote q = new Quote();
        q.setType("success");
        Value v = new Value();
        v.setId(1l);
        v.setJoke("Chuck Norris is right now sleeping, but he can see you with both eyes closed...so behave!");
        q.setValue(v);
        return q;
    }
}
