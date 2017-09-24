FROM openjdk:8
MAINTAINER prateek goel
VOLUME /tmp

WORKDIR /

ADD target/uaa-server.jar uaa-server.jar

RUN bash -c  'touch uaa.jar'

ENTRYPOINT  ["java" ,  "-Dspring.data.mongodb.uri=mongodb://uaa-mongodb/uam" ,"-Djava.security.egd=file:/dev/./urandom" , "-jar" , "uaa-server.jar"]