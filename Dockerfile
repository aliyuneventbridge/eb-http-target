# First stage: complete build environment
FROM maven:3.5.0-jdk-8-alpine AS builder

# add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./src src/

# package jar
RUN mvn clean package

FROM openjdk:8-jdk-alpine

COPY --from=builder target/http-target-0.0.1-SNAPSHOT.jar http-target-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","http-target-0.0.1-SNAPSHOT.jar"]