
FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com
VOLUME /tmp

ADD target/service-registry.jar /service-registry.jar

RUN bash -c  'touch service-registry.jar'

ENTRYPOINT  ["java"  ,"-Djava.security.egd=file:/dev/./urandom" , "-jar" , "service-registry.jar"]