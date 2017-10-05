package com.softcell.gonogo.uaaserver.security;

import com.softcell.gonogo.uaaserver.service.HybridUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class OAuth2MongoDBConfiguration extends GlobalAuthenticationConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2MongoDBConfiguration.class);

    @Autowired
    private HybridUserDetailsService hybridUserDetailsService;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mongoDbUserDetailsService()).passwordEncoder(passwordEncoder);
        hybridUserDetailsService.addService(mongoDbUserDetailsService());
    }


    @Bean
    public UserDetailsService mongoDbUserDetailsService() {
        return new MongoDbUserDetailsService();
    }


}
