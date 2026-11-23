package com.aver.quote.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aver.quote.domain.Joke;
import com.aver.quote.domain.Quote;
import com.aver.quote.service.QuoteService;

@WebMvcTest(QuoteController.class)
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void returnsTheQuoteAsJson() throws Exception {
        Joke value = new Joke();
        value.setId("abc123");
        value.setJoke("a joke");
        Quote quote = new Quote();
        quote.setType("success");
        quote.setValue(value);
        when(quoteService.getRandomQuote()).thenReturn(quote);

        mockMvc.perform(get("/quotes/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("success"))
                .andExpect(jsonPath("$.value.id").value("abc123"))
                .andExpect(jsonPath("$.value.joke").value("a joke"));
    }

    @Test
    void returnsNotFoundForAnUnknownPath() throws Exception {
        mockMvc.perform(get("/does-not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsMethodNotAllowedForAnUnsupportedMethod() throws Exception {
        mockMvc.perform(post("/quotes/"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void returnsAServerErrorBodyWhenTheServiceThrows() throws Exception {
        when(quoteService.getRandomQuote()).thenThrow(new IllegalStateException("boom"));

        mockMvc.perform(get("/quotes/"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0].code").value("system.error"));
    }
}
