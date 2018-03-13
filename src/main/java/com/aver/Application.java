package com.aver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mathew on 10/25/15.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@RestController
@EnableHystrix
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    //
    // @Bean
    // protected AWSXRayServletFilter getAWSXRayServletFilter() {
    // return new AWSXRayServletFilter();
    // }

    // @Bean
    // public FilterRegistrationBean myFilterRegistration() {
    // FilterRegistrationBean registration = new FilterRegistrationBean();
    // registration.setDispatcherTypes(DispatcherType.REQUEST);
    // registration.setFilter(new AWSXRayServletFilter());
    // registration.addUrlPatterns("/quotes/*");
    // registration.addInitParameter("paramName", "paramValue");
    // registration.setName("XRayServletFilter");
    // registration.setOrder(1);
    // return registration;
    // }
}
