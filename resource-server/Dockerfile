FROM java:8-jre-alpine
MAINTAINER prateekg@softcell.com
VOLUME /tmp


ENV USER_NAME prateek
ENV APP_HOME /home/${USER_NAME}/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD target/.jar $APP_HOME/resource-server.jar
RUN chown $USER_NAME $APP_HOME/resource-server.jar

USER $USER_NAME
WORKDIR $APP_HOME

RUN bash -c  'touch resource-server.jar'

ENTRYPOINT  ["java"  ,"-Djava.security.egd=file:/dev/./urandom" , "-jar" , "resource-server.jar"]