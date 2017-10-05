package com.softcell.gonogo.uaaserver.config;

import com.softcell.gonogo.uaaserver.security.CustomTokenEnhancer;
import com.softcell.gonogo.uaaserver.security.IatTokenEnhancer;
import com.softcell.gonogo.uaaserver.service.ClientDetailService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by prateek on 23/5/17.
 */
@Configuration
@EnableAuthorizationServer
public class UaaConfiguration extends AuthorizationServerConfigurerAdapter implements ApplicationContextAware {

    /**
     * Access tokens will not expire any earlier than this.
     */
    private static final int MIN_ACCESS_TOKEN_VALDITIY_SECS = 60;

    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("clientDetailService")
    private ClientDetailService clientDetailService;


    @Autowired
    private UaaProperties uaaProperties;

    @Autowired
    @Qualifier("hybridUserDetailsService")
    private UserDetailsService hybridUserDetailsService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Setup the client application which attempts to get access to user's
     * account after user permission.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.withClientDetails(clientDetailService);

    }

    /**
     * We set our authorization storage feature specifying that we would use the
     * NOSQL store for token and authorization code storage.<br>
     * <br>
     * <p>
     * We also attach the {@link AuthenticationManager} so that password grants
     * can be processed.
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off

        //pick up all  TokenEnhancers incl. those defined in the application
        //this avoids changes to this class if an application wants to add its own to the chain
        Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));

        endpoints.tokenStore(tokenStore())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(hybridUserDetailsService);
        // @formatter:on
    }

    /**
     * Apply the token converter (and enhancer) for token store.
     *
     * @return the JwtTokenStore managing the tokens.
     */
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * This bean generates an token enhancer, which manages the exchange between JWT acces tokens and Authentication
     * in both directions.
     *
     * @return an access token converter configured with the authorization server's public/private keys
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource(uaaProperties.getKeyStore().getName()), uaaProperties.getKeyStore().getPassword().toCharArray()
        ).getKeyPair(uaaProperties.getKeyStore().getAlias());

        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public TokenEnhancer organisationEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public TokenEnhancer iatEnhancer() {
        return new IatTokenEnhancer();
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @EnableResourceServer
    public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        private final TokenStore tokenStore;

        private final UaaProperties uaaProperties;

        private final CorsFilter corsFilter;

        public ResourceServerConfiguration(TokenStore tokenStore, UaaProperties uaaProperties, CorsFilter corsFilter) {
            this.tokenStore = tokenStore;
            this.uaaProperties = uaaProperties;
            this.corsFilter = corsFilter;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .and()
                    .csrf()
                    .disable()
                    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .headers()
                    .frameOptions()
                    .disable()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/activate").permitAll()
                    .antMatchers("/api/authenticate").permitAll()
                    .antMatchers("/api/account/reset-password/init").permitAll()
                    .antMatchers("/api/account/reset-password/finish").permitAll()
                    .antMatchers("/api/profile-info").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/management/health").permitAll()
                    .antMatchers("/management/**").hasAuthority("ADMIN")
                    .antMatchers("/v2/api-docs/**").permitAll()
                    .antMatchers("/swagger-resources/configuration/ui").permitAll()
                    .antMatchers("/swagger-ui/index.html").hasAuthority("ADMIN");
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("uaa-server").tokenStore(tokenStore);
        }
    }


}
