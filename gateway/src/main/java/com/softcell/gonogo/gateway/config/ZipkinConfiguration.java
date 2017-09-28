package com.softcell.gonogo.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZipkinConfiguration {

    @Autowired
    public DiscoveryClient discoveryClient;


    @Autowired
    private ZipkinProperties zipkinProperties;

    @Bean
    public Sampler sampler(){
        return info -> true;
    }


}
