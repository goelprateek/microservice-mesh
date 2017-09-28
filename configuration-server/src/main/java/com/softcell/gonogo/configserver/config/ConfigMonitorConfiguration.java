package com.softcell.gonogo.configserver.config;

import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("config-monitor")
@Configuration
@Import(KafkaAutoConfiguration.class)
public class ConfigMonitorConfiguration {


}
