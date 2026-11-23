package com.aver.notetaker.services.notes;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aver.notetaker.domain.Quote;
import com.aver.notetaker.domain.Value;

@WebMvcTest(QuoteServiceEndPoint.class)
class QuoteServiceEndPointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void returnsTheQuoteAsJson() throws Exception {
        Value value = new Value();
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
    void returnsAServerErrorBodyWhenTheServiceThrows() throws Exception {
        when(quoteService.getRandomQuote()).thenThrow(new IllegalStateException("boom"));

        mockMvc.perform(get("/quotes/"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0].code").value("system.error"));
    }
}
