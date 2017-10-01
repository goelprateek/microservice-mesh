package com.softcell.gonogo.uaaserver.config.security;

import com.softcell.gonogo.uaaserver.config.CustomTokenEnhancer;
import com.softcell.gonogo.uaaserver.service.ClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

/**
 * Created by prateek on 23/5/17.
 */
@Configuration
@EnableAuthorizationServer
@EnableWebSecurity
@Order(2)
@RefreshScope
public class UaaConfiguration extends AuthorizationServerConfigurerAdapter {


    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("clientDetailService")
    private ClientDetailService clientDetailService;


    @Autowired
    private UserDetailsService hybridUserDetailsService;

    @Value("${gng.security.jwt.pass}")
    private String passKey;



    /*@EnableResourceServer
    public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        private final TokenStore tokenStore;

        private final CorsFilter corsFilter;

        public ResourceServerConfiguration(TokenStore tokenStore, CorsFilter corsFilter) {
            this.tokenStore = tokenStore;
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
                    .antMatchers("/management/**").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/v2/api-docs/**").permitAll()
                    .antMatchers("/swagger-resources/configuration/ui").permitAll()
                    .antMatchers("/swagger-ui/index.html").hasAuthority("ROLE_ADMIN");
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("uaa-resource").tokenStore(tokenStore);
        }
    }*/

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.realm("auth-server")
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }


    /**
     * Setup the client application which attempts to get access to user's
     * account after user permission.
     */
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {// @formatter:off

        clients.withClientDetails(clientDetailService);

    } // @formatter:on


    /**
     * We set our authorization storage feature specifying that we would use the
     * NOSQL store for token and authorization code storage.<br>
     * <br>
     *
     * We also attach the {@link AuthenticationManager} so that password grants
     * can be processed.
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(hybridUserDetailsService);
        // @formatter:on
    }


    /**
     * The OAuth2 tokens are defined in the datasource defined in the
     * <code>auth-server.yml</code> file stored in the Spring Cloud config
     * github repository.
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), passKey.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public UserApprovalHandler approvalHandler() {
        UserApprovalHandler approvalHandler = new DefaultUserApprovalHandler();
        return approvalHandler;

    }

    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    @Bean
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setRealmName("auth-server");
        return oAuth2AuthenticationEntryPoint;
    }

}
