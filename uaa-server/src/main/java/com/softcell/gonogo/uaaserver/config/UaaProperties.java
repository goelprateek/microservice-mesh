package com.softcell.gonogo.uaaserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "uaa", ignoreUnknownFields = false)
@Data
public class UaaProperties {


    public KeyStore keyStore = new KeyStore();

    public Security security = new Security();

    public Swagger swagger = new Swagger();

    public Mail mail = new Mail();

    public Ribbon ribbon = new Ribbon();

    public Async async = new Async();


    public WebClientConfiguration webClientConfiguration = new WebClientConfiguration();

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

    /**
     * Keystore configuration for signing and verifying JWT tokens.
     */
    @Data
    public static class KeyStore {

        //name of the keystore in the classpath
        private String name = "jwt.jks";

        //password used to access the key
        private String password = "password";

        //name of the alias to fetch
        private String alias = "jwt";

    }

    @Data
    public static class WebClientConfiguration {

        //validity of the short-lived access token in secs (min: 60), don't make it too long
        private int accessTokenValidityInSeconds = 5 * 60;

        //validity of the refresh token in secs (defines the duration of "remember me")
        private int refreshTokenValidityInSecondsForRememberMe = 7 * 24 * 60 * 60;

        private String clientId = "uiapplication";

        private String secret = "secret";

    }

    @Data
    public static class Security {

        private final RememberMe rememberMe = new RememberMe();

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
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
    public static class Mail {

        private String from = "";

        private String baseUrl = "";

    }

    @Data
    public static class Ribbon {

        private String[] displayOnActiveProfiles;


    }

    @Data
    public static class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

    }


}
