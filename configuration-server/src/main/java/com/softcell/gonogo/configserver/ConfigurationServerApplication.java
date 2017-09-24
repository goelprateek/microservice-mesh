package com.softcell.gonogo.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableAutoConfiguration
public class ConfigurationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationServerApplication.class, args);
    }
}
