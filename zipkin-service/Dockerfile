FROM java:8-jre-alpine
MAINTAINER prateek goel



ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APP_SLEEP=0 \
    JAVA_OPTS=""

WORKDIR /

ADD @project.build.finalName@.jar /zipkin-service.jar

CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} \
        -Djava.security.egd=file:/dev/./urandom  \
        -Dspring.profiles.active=docker \
    -jar /zipkin-service.jar

