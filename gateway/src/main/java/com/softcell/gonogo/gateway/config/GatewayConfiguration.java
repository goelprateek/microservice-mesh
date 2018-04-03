package com.softcell.gonogo.gateway.config;


import com.softcell.gonogo.gateway.gateway.accesscontroll.AccessControlFilter;
import com.softcell.gonogo.gateway.gateway.ratelimiting.RateLimitingFilter;
import com.softcell.gonogo.gateway.gateway.responserewriting.SwaggerBasePathRewritingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfiguration.class);

    private final GoNoGoProperties goNoGoProperties;


    public GatewayConfiguration(GoNoGoProperties goNoGoProperties) {
        this.goNoGoProperties = goNoGoProperties;
    }

    /**
     * Configures the Zuul filter that limits the number of API calls per user.
     * <p>
     * This uses Bucke4J to limit the API calls, see {@link com.softcell.gonogo.gateway.gateway.ratelimiting.RateLimitingFilter}.
     */
    @Configuration
    @ConditionalOnProperty("gonogo.gateway.rate-limiting.enabled")
    public static class RateLimitingConfiguration {

        private final GoNoGoProperties goNoGoProperties;

        public RateLimitingConfiguration(GoNoGoProperties goNoGoProperties) {
            this.goNoGoProperties = goNoGoProperties;
        }

        @Bean
        public RateLimitingFilter rateLimitingFilter() {
            return new RateLimitingFilter(goNoGoProperties);
        }
    }


    @Configuration
    public static class SwaggerBasePathRewritingConfiguration {

        @Bean
        public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter() {
            return new SwaggerBasePathRewritingFilter();
        }
    }


    @Configuration
    public static class AccessControlFilterConfiguration {

        @Bean
        public AccessControlFilter accessControlFilter(RouteLocator routeLocator, GoNoGoProperties goNoGoProperties) {
            return new AccessControlFilter(routeLocator, goNoGoProperties);
        }
    }


}
