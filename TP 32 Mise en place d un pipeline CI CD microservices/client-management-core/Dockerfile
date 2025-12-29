# Phase de construction - Build Stage
FROM maven:3.8.5-openjdk-17 AS build-stage
WORKDIR /application
COPY ./pom.xml .
COPY ./src ./src
RUN mvn clean package -DskipTests

# Phase d'exécution - Runtime Stage
FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="fatimaezzahrakld <fatimaezzahrakld@github.com>"
LABEL version="2.0.0"
LABEL description="Client Management Microservice"
VOLUME /tmp
WORKDIR /application
COPY --from=build-stage /application/target/*.jar client-management.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "client-management.jar"]
