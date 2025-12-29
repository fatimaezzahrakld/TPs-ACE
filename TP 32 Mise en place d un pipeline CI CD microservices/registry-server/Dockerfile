# Phase de construction - Build Stage
FROM maven:3.8.5-openjdk-17 AS build-stage
WORKDIR /application
COPY ./pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

# Phase d'exécution - Runtime Stage
FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="fatimaezzahrakld <fatimaezzahrakld@github.com>"
LABEL version="2.0.0"
LABEL description="Service Registry Server - Eureka"
WORKDIR /application
COPY --from=build-stage /application/target/*.jar registry-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "registry-server.jar"]
