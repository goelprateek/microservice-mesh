package com.softcell.gonogo.resourceserver.config;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Configuration for this resource server.
 * The @EnableResourceServer annotation enables the Spring Security filter that authenticates requests via an incoming OAuth2 token.
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceServerConfig.class);


    /**
     * anything  is protected by a valid Oauth2 token.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        String id = resourceServerResources().resourceServerProperties.getId();
        resources.resourceId(id)
                .tokenServices(tokenService())
                .tokenStore(tokenStore());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenService() throws Exception {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setRefreshTokenValiditySeconds(resourceServerResources().getRefreshTokenValidityInSeconds());
        defaultTokenServices.setAccessTokenValiditySeconds(resourceServerResources().getAccessTokenValidityInSeconds());
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


    @Bean
    //@Qualifier("tokenStore")
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * Defining a name allows us to use "gng.xxx" instead of "security.oauth2.xxx" in the application.yml file
     */
    @Bean
    @ConfigurationProperties("gng")
    public ResourceServerResources resourceServerResources() {
        return new ResourceServerResources();
    }


    /**
     * Configure the bean responsible for loading/decoding our access tokens.
     * We need the public key of GNG Identity Manager during the creation of the bean as Spring will not be able to decode the token
     * if the signature verification is failing.
     */
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        String publicKey = null;

        try {

            publicKey = revealPublicKey();


        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.error(" exception occur while reading public key from authorization server with probable cause {} ", e.getMessage());
        }

        jwtAccessTokenConverter.setVerifierKey(publicKey);
        jwtAccessTokenConverter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier());
        jwtAccessTokenConverter.afterPropertiesSet();

        return jwtAccessTokenConverter;

    }

    private String revealPublicKey() throws IOException {


        String url = resourceServerResources().getCheckTokenUri();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        LOGGER.debug(" fetch public key from {}", builder.toUriString());

        String forObject = restTemplate().getForObject(builder.toUriString(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(forObject);

        JsonNode value = jsonNode.get("value");
        String publicKey = value.asText();

        LOGGER.debug("  public key obtained from authorization server {} ", publicKey);

        return publicKey;
    }

    @Bean
    public JwtClaimsSetVerifier jwtClaimsSetVerifier() {
        return new DelegatingJwtClaimsSetVerifier(Arrays.asList(issuerClaimVerifier(), customJwtClaimVerifier()));
    }

    @Bean
    public JwtClaimsSetVerifier issuerClaimVerifier() {
        try {
            return new IssuerClaimVerifier(new URL("http://localhost:8081"));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JwtClaimsSetVerifier customJwtClaimVerifier() {
        return new CustomClaimVerifier();
    }


    /**
     * To access the resources properties defined in application.yml file.
     */
    class ResourceServerResources {

        @NestedConfigurationProperty
        private ResourceServerProperties resourceServerProperties = new ResourceServerProperties();

        @Value("${gng.resource.localValidation}")
        private boolean performLocalValidation;

        @Value("${gng.resource.checkTokenUri}")
        private String checkTokenUri;

        @Value("${gng.resource.access-token-validity}")
        private int accessTokenValidityInSeconds;

        @Value("${gng.resource.refresh-token-validity}")
        private int refreshTokenValidityInSeconds;


        public ResourceServerProperties getResourceServerProperties() {
            return resourceServerProperties;
        }

        public boolean isPerformLocalValidation() {
            return performLocalValidation;
        }

        public String getCheckTokenUri() {
            return checkTokenUri;
        }

        public int getAccessTokenValidityInSeconds() {
            return accessTokenValidityInSeconds;
        }

        public int getRefreshTokenValidityInSeconds() {
            return refreshTokenValidityInSeconds;
        }
    }

}
