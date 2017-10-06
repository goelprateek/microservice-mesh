FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

WORKDIR /

ADD target/resource-server.jar /resource-server.jar


CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /resource-server.jar

