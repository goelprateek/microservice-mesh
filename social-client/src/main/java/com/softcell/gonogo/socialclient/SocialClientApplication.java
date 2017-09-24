package com.softcell.gonogo.socialclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableOAuth2Client
@RestController
@EnableDiscoveryClient
public class SocialClientApplication extends WebSecurityConfigurerAdapter{

    public static void main(String[] args) {
        SpringApplication.run(SocialClientApplication.class, args);
    }

    @Autowired
    @Qualifier("oauth2ClientContext")
    public OAuth2ClientContext oauth2ClientContext;


    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping("/unauthenticated")
    public String unauthenticated() {
        return "redirect:/?error=true";
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/" , "/login**", "/webjars/**").permitAll()
                .anyRequest().authenticated().and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }




    private Filter ssoFilter() {

        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();

        // facebook filter
        OAuth2ClientAuthenticationProcessingFilter fbFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
        OAuth2RestTemplate fbRestTemplate = new OAuth2RestTemplate(facebook(),oauth2ClientContext);
        fbFilter.setRestTemplate(fbRestTemplate);
        UserInfoTokenServices tokenService = new UserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
        tokenService.setRestTemplate(fbRestTemplate);
        fbFilter.setTokenServices(tokenService);
        filters.add(fbFilter);


        // github filter
        OAuth2ClientAuthenticationProcessingFilter githubFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/login/github");
        OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(github(), oauth2ClientContext);
        githubFilter.setRestTemplate(githubTemplate);
        tokenService = new UserInfoTokenServices(githubResource().getUserInfoUri(), github().getClientId());
        tokenService.setRestTemplate(githubTemplate);
        githubFilter.setTokenServices(tokenService);
        filters.add(githubFilter);

        // google filter
        OAuth2ClientAuthenticationProcessingFilter googleFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        tokenService = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenService.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenService);
        filters.add(googleFilter);

        filter.setFilters(filters);

        return filter;

    }

    @Bean
    @ConfigurationProperties("facebook.client")
    public AuthorizationCodeResourceDetails facebook(){
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("facebook.resource")
    public ResourceServerProperties facebookResource(){
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("github.client")
    public AuthorizationCodeResourceDetails github() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("github.resource")
    public ResourceServerProperties githubResource() {
        return new ResourceServerProperties();
    }


    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google(){
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }
}
