FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com
WORKDIR /


ADD @project.build.finalName@.jar /service-registry.jar


CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} \
        -Djava.security.egd=file:/dev/./urandom \
        -Dspring.profiles.active=docker \
    -jar /service-registry.jar
