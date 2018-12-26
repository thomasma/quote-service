package com.aver.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

@Data
@Component
@ConfigurationProperties("server")
@ToString
public class ApplicationProperties {
    private String name;
    private String quoteSvcPath;
    private String quoteSvcURI;
}