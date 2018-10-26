FROM openjdk:8-jre-alpine
MAINTAINER domi_yang@hotmail.com

#default download from git repo, when --build-arg mp4jar_url=target/mp4.jar will pick the target/mp4.jar from local path
ARG mp4jar_url=https://github.com/domiyang/mp4/raw/mp4-springboot/target/mp4.jar

#packet the mp4.jar
ADD ${mp4jar_url} app.jar

#expected the volumens mapped from host to container as /mp4
ENTRYPOINT ["/usr/bin/java", "-Dmp4.media.path=/mp4", "-jar", "app.jar"]

EXPOSE 8080
