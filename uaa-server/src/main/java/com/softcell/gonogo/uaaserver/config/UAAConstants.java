package com.softcell.gonogo.uaaserver.config;

public class UAAConstants {

    // Spring profiles for development, test and production
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";

    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_SWAGGER = "swagger";


}
