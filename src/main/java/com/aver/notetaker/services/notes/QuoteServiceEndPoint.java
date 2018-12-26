package com.aver.notetaker.services.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aver.notetaker.domain.Quote;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/quotes")
@Slf4j
public class QuoteServiceEndPoint {

    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Quote getQuote() {
        log.info("Got /quotes request for a new random joke. About to deliver one.");
        return quoteService.getRandomQuote();
    }
}
