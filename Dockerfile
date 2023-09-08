FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim
MAINTAINER youse.co.ke
COPY target/yousebe.jar yousebe/yousebe.jar
ENTRYPOINT ["java", "-jar","yousebe/yousebe.jar"]
EXPOSE 8082