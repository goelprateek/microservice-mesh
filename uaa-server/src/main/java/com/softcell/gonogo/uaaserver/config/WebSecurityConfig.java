package com.softcell.gonogo.uaaserver.config;

import com.softcell.gonogo.uaaserver.service.UserAuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by prateek on 23/5/17.
 */
@Configuration
@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthProviderService userAuthProviderService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // @formatter:off
        // mongodb authentication provider

        auth.authenticationProvider(userAuthProviderService);

        // ldap authentication provider
        /*auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource(contextSource())
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute("userPassword");*/

        // @formatter:on
    }

    /*@Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return  new DefaultSpringSecurityContextSource(
                Arrays.asList("ldap://localhost:8389/"), "dc=springframework,dc=org");
    }*/



    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off

        /*http.csrf()
                .disable()
                .formLogin().loginPage("/login").permitAll().and()
                .requestMatchers().antMatchers("/login" , "/oauth/authorize", "/oauth/confirm_access")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();*/

        http.formLogin().loginPage("/login").permitAll().and().authorizeRequests()
                .anyRequest().authenticated();

        // @formatter:on
    }

}
