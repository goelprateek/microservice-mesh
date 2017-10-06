FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com
WORKDIR /


ADD target/service-registry.jar /service-registry.jar


CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /service-registry.jar
