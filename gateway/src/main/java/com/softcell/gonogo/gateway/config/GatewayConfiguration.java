package com.softcell.gonogo.gateway.config;


import com.softcell.gonogo.gateway.gateway.accesscontroll.AccessControlFilter;
import com.softcell.gonogo.gateway.gateway.ratelimiting.RateLimitingFilter;
import com.softcell.gonogo.gateway.gateway.responserewriting.SwaggerBasePathRewritingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfiguration.class);

    private final GoNoGoProperties goNoGoProperties;

    @Autowired
    private RouteLocator routeLocator;


    public GatewayConfiguration(GoNoGoProperties goNoGoProperties) {
        this.goNoGoProperties = goNoGoProperties;
    }

    @Bean
    public RateLimitingFilter rateLimitingFilter() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" initializing RateLimiting Filter  ");
        }

        return new RateLimitingFilter(goNoGoProperties);
    }


    @Bean
    public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter() {
        return new SwaggerBasePathRewritingFilter();
    }


    @Bean
    public AccessControlFilter accessControlFilter(RouteLocator routeLocator, GoNoGoProperties goNoGoProperties) {
        return new AccessControlFilter(routeLocator, goNoGoProperties);
    }


}
