FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com
VOLUME /tmp


ADD target/gateway.jar /gateway.jar
WORKDIR /

RUN bash -c  'touch gateway.jar'

ENTRYPOINT  ["java"  ,"-Djava.security.egd=file:/dev/./urandom" , "-jar" , "gateway.jar"]