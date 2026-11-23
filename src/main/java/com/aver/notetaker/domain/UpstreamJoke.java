package com.aver.notetaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Response of the upstream joke API (https://api.chucknorris.io/jokes/random).
 * Only the fields we use are mapped.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UpstreamJoke {

    private String id;
    private String value;
}
