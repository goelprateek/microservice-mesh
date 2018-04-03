package com.softcell.gonogo.uaaserver.security;

import com.softcell.gonogo.uaaserver.service.HybridUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;

//@Configuration
public class OAuth2LDAPConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private HybridUserDetailsService hybridUserDetailsService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .passwordCompare().passwordEncoder(new LdapShaPasswordEncoder()).passwordAttribute("userPassword").and()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .userDetailsContextMapper(userDetailsMapper()).contextSource().ldif("classpath:schema.ldif");
    }

    @Bean
    public UserDetailsService ldapUserDetailsService() {
        final DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
                "ldap://127.0.0.1:33389/dc=springframework,dc=org");
        contextSource.afterPropertiesSet();

        final FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch("", "uid={0}", contextSource);
        final LdapUserDetailsService service = new LdapUserDetailsService(userSearch);
        service.setUserDetailsMapper(userDetailsMapper());

        hybridUserDetailsService.addService(service);

        return service;
    }

    @Bean
    public LdapUserDetailsMapper userDetailsMapper() {
        return new LdapUserDetailsMapper();
    }
}
