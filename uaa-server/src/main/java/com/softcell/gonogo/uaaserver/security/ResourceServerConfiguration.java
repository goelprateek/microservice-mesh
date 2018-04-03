package com.softcell.gonogo.uaaserver.security;

//@Configuration
//@EnableResourceServer
//@Order(3)
public class ResourceServerConfiguration
        //       extends ResourceServerConfigurerAdapter
{

    /*private final TokenStore tokenStore;

    private final CorsFilter corsFilter;

    @Autowired
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
                //.antMatchers("/api/register").permitAll()
                //.antMatchers("/api/activate").permitAll()
                //.antMatchers("/api/authenticate").permitAll()
                //.antMatchers("/api/account/reset-password/init").permitAll()
                //.antMatchers("/api/account/reset-password/finish").permitAll()
                //.antMatchers("/api/profile-info").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/**").hasAuthority("USER_ADMIN")
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                .antMatchers("/swagger-ui/index.html").hasAuthority("USER_ADMIN");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("auth-server").tokenStore(tokenStore);
    }*/
}
