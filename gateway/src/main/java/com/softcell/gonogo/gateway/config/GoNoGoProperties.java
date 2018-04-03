package com.softcell.gonogo.gateway.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "gonogo", ignoreUnknownFields = false)
@Data
public class GoNoGoProperties {

    private final Swagger swagger = new Swagger();

    private final Gateway gateway = new Gateway();

    private final Cache cache = new Cache();

    private final Logging logging = new Logging();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final Security security = new Security();

    private final Ribbon ribbon = new Ribbon();

    @Data
    public static class Swagger {
        private String title = "Application API";
        private String description = "API documentation";
        private String version = "0.0.1";
        private String termsOfServiceUrl;
        private String contactName;
        private String contactUrl;
        private String contactEmail;
        private String license;
        private String licenseUrl;
        private String defaultIncludePattern = "/api/.*";
        private String host;
        private String[] protocols = new String[0];
    }

    @Data
    public static class Gateway {
        private final GoNoGoProperties.Gateway.RateLimiting rateLimiting = new GoNoGoProperties.Gateway.RateLimiting();
        private Map<String, List<String>> authorizedMicroservicesEndpoints = new LinkedHashMap();

        @Data
        public static class RateLimiting {
            private boolean enabled = false;
            private long limit = 100000L;
            private int durationInSeconds = 3600;

        }
    }

    @Data
    public static class Cache {

        private final Hazelcast hazelcast = new Hazelcast();

        @Data
        public static class Hazelcast {
            private int timeToLiveSeconds = 3600;
            private int backupCount = 1;
        }
    }

    @Data
    public static class Logging {

        private final Logstash logstash = new Logstash();


        @Data
        public static class Logstash {

            private boolean enabled = false;
            private String host = "localhost";
            private int port = 5000;
            private int queueSize = 512;

        }


    }

    @Data
    public static class Security {

        private final RememberMe rememberMe = new RememberMe();

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        @Data
        public static class ClientAuthorization {

            private String accessTokenUri;

            private String tokenServiceId;

            private String clientId;

            private String clientSecret;

        }

        @Data
        public static class Authentication {

            private final Oauth oauth = new Oauth();

            private final Jwt jwt = new Jwt();


            @Data
            public static class Oauth {

                private String clientId;

                private String clientSecret;

                private int tokenValidityInSeconds = 1800;

            }

            @Data
            public static class Jwt {

                private String secret;

                private long tokenValidityInSeconds = 1800;

                private long tokenValidityInSecondsForRememberMe = 2592000;

            }
        }

        @Data
        public static class RememberMe {

            @NotNull
            private String key;

        }
    }

    @Data
    public static class Ribbon {

        private String[] displayOnActiveProfiles;


    }


}
