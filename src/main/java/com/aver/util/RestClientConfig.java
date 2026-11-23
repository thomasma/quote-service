package com.aver.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    /**
     * RestTemplate with timeouts, so that a slow or hung upstream cannot block
     * request threads indefinitely. See quote.connect-timeout and
     * quote.read-timeout.
     */
    @Bean
    public RestTemplate rest(RestTemplateBuilder builder, ApplicationProperties properties) {
        return builder.connectTimeout(properties.getConnectTimeout()).readTimeout(properties.getReadTimeout())
                .build();
    }
}
