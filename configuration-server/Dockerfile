FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APP_SLEEP=0 \
    JAVA_OPTS=""

WORKDIR /

ADD @project.build.finalName@.jar /configuration-server.jar

EXPOSE 7070

CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} \
        -Djava.security.egd=file:/dev/./urandom  \
        -Dspring.profiles.active=docker \
    -jar /configuration-server.jar



