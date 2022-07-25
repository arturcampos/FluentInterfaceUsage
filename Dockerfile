FROM maven:3.6.3-jdk-11-slim as builder

COPY src /home/app/src

COPY pom.xml /home/app

RUN mvn -f /home/app clean package

FROM openjdk:11-jre-slim

COPY --from=builder /home/app/target/capitalgains-final.jar /capitalgains-final.jar
ENTRYPOINT ["java","-jar","/capitalgains-final.jar"]