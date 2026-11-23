package com.aver.quote.config;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /**
     * Applies the connect and read timeouts, so that a slow or hung upstream cannot block request threads
     * indefinitely. See quote.connect-timeout and quote.read-timeout.
     * <p>
     * This is a customizer, ordered first, so that anything that replaces the request factory afterwards (such as the
     * mock server in the tests) takes precedence.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public RestClientCustomizer upstreamTimeoutsCustomizer(QuoteProperties properties) {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withConnectTimeout(properties.getConnectTimeout())
                .withReadTimeout(properties.getReadTimeout());
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.detect().build(settings);
        return builder -> builder.requestFactory(requestFactory);
    }

    @Bean
    public RestClient quoteRestClient(RestClient.Builder builder) {
        return builder.build();
    }
}
