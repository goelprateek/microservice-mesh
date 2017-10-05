package com.softcell.gonogo.uaaserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailConfiguration.class);

    private final UaaProperties uaaProperties;


    public MailConfiguration(UaaProperties uaaProperties) {
        this.uaaProperties = uaaProperties;
    }

    @Bean
    public JavaMailSender javaMailSender() {

        return new JavaMailSenderImpl();
    }

}
