package com.aver.notetaker.services.notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aver.notetaker.domain.Quote;

@RestController
@RequestMapping("/quotes")
public class QuoteServiceEndPoint {
    protected final static Logger LOGGER = LoggerFactory
            .getLogger(QuoteServiceEndPoint.class);

    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Quote getQuote() {
        return quoteService.getRandomQuote();
    }
}
