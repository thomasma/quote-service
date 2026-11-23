package com.aver.quote.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aver.quote.domain.Quote;
import com.aver.quote.service.QuoteService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/quotes")
@Slf4j
public class QuoteController {

    /** Response header set to "true" when the quote is the canned fallback, not a real joke from the upstream. */
    public static final String FALLBACK_HEADER = "X-Fallback";

    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Quote> getQuote() {
        log.info("Got /quotes request for a new random joke. About to deliver one.");
        Quote quote = quoteService.getRandomQuote();

        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        if (quote.isFallback()) {
            response.header(FALLBACK_HEADER, "true");
        }
        return response.body(quote);
    }
}
