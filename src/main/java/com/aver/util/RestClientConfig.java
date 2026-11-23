package com.aver.util;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    /**
     * RestTemplate with timeouts, so that a slow or hung upstream cannot block
     * request threads indefinitely. Override with quote.connect-timeout and
     * quote.read-timeout.
     */
    @Bean
    public RestTemplate rest(RestTemplateBuilder builder,
            @Value("${quote.connect-timeout:2s}") Duration connectTimeout,
            @Value("${quote.read-timeout:3s}") Duration readTimeout) {
        return builder.connectTimeout(connectTimeout).readTimeout(readTimeout).build();
    }
}
