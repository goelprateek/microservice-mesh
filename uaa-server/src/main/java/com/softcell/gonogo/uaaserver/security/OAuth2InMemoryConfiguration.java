package com.softcell.gonogo.uaaserver.security;

import com.softcell.gonogo.uaaserver.service.HybridUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class OAuth2InMemoryConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private HybridUserDetailsService hybridUserDetailsService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("padd").roles("ADMIN").and().getUserDetailsService();
        hybridUserDetailsService.addService(auth.getDefaultUserDetailsService());
    }
}
