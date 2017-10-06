FROM java:8-jre-alpine
MAINTAINER prateek goel



ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APP_SLEEP=0 \
    JAVA_OPTS=""

WORKDIR /


ADD target/hystrix-dashboard.jar /hystrix-dashboard.jar

EXPOSE 7074

CMD echo " The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom  -jar /hystrix-dashboard.jar