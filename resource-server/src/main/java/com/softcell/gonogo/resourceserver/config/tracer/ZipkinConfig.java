package com.softcell.gonogo.resourceserver.config.tracer;

import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZipkinConfig {

    @Bean
    public AlwaysSampler alwaysSampler(){
        return new AlwaysSampler();
    }
}
