FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com

ADD target/configuration-server.jar configuration-server.jar

VOLUME /tmp

VOLUME /target

RUN bash -c  'touch configuration-server.jar'

EXPOSE 9092

ENTRYPOINT  ["java"  ,"-Djava.security.egd=file:/dev/./urandom" , "-jar" , "configuration-server.jar"]