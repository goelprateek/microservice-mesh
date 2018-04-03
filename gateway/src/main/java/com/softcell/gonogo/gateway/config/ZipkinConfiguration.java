package com.softcell.gonogo.gateway.config;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ZipkinConfiguration {

    @Autowired
    public DiscoveryClient discoveryClient;


    @Autowired
    private ZipkinProperties zipkinProperties;


    @Autowired
    @Qualifier("spanReporterCounterService")
    private SpanMetricReporter spanMetricReporter;


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }




    @Bean
    @Primary
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public HystrixCommandAspect hystrixCommandAspect(){
        return new HystrixCommandAspect();
    }

}
