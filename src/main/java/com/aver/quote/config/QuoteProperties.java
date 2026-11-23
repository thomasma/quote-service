package com.aver.quote.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Settings for the upstream joke service, bound from the {@code quote.*} properties.
 */
@Data
@Component
@ConfigurationProperties("quote")
public class QuoteProperties {

    /** Base URI of the upstream joke service, e.g. https://api.chucknorris.io */
    private String serviceUri;

    /** Path of the random joke endpoint on the upstream service. */
    private String servicePath;

    /** How long to wait to connect to the upstream service. */
    private Duration connectTimeout = Duration.ofSeconds(2);

    /** How long to wait for the upstream service to respond. */
    private Duration readTimeout = Duration.ofSeconds(3);
}
