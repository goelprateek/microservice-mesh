package com.softcell.gonogo.gateway.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

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


}
