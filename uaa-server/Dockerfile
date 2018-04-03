FROM java:8-jre-alpine
MAINTAINER prateek goel



ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APP_SLEEP=0 \
    JAVA_OPTS=""

WORKDIR /

ADD @project.build.finalName@.jar /uaa-server.jar

CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} \
        -Djava.security.egd=file:/dev/./urandom \
        -Dspring.data.mongodb.uri=mongodb://mongodb/uam \
        -Dspring.profiles.active=docker \
    -jar /uaa-server.jar

