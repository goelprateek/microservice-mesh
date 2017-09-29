package com.softcell.gonogo.gateway.config.tracer;


import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZipkinConfig {

    @Bean
    public AlwaysSampler sampler(){
        return new AlwaysSampler();
    }
}
