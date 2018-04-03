
FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com
WORKDIR /

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

ADD @project.build.finalName@.jar /social-client.jar

CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} \
        -Djava.security.egd=file:/dev/./urandom \
        -Dspring.profiles.active=docker \
    -jar /social-client.jar



