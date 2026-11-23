package com.aver.quote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Quote {
    private String type;
    private Joke value;

    /**
     * True when this is the canned fallback quote, not a joke from the upstream service. Not part of the JSON body:
     * the controller reports it in the X-Fallback response header.
     */
    @JsonIgnore
    private boolean fallback;
}
