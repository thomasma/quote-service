package com.aver.notetaker.services.notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aver.notetaker.domain.Quote;
import com.aver.notetaker.domain.Value;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class QuoteServiceImpl implements QuoteService {
    protected final static Logger LOGGER = LoggerFactory
            .getLogger(QuoteServiceImpl.class);

    /**
     * Call a 3rd party internet service to retrieve a random chuck norris joke.
     * 
     * @return a random quote
     */
    @HystrixCommand(fallbackMethod = "fallbackQuoteService")
    public Quote getRandomQuote() {
        LOGGER.info("Getting a random quote.");
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate
                .getForObject(
                        "http://api.icndb.com/jokes/random?firstName=Chuck&amp;lastName=Doe",
                        Quote.class);
        LOGGER.debug(quote.toString());
        return quote;
    }

    private Quote fallbackQuoteService() {
        Quote q = new Quote();
        q.setType("success");
        Value v = new Value();
        v.setId(1l);
        v.setJoke("Chuck Norris is right now sleeping but has one eye open so don't party yet!");
        return q;
    }
}
