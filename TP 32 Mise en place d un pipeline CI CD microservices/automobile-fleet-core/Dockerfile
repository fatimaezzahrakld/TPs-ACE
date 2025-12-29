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
LABEL description="Fleet Management Microservice"
WORKDIR /application
ARG JAR_FILE=target/*.jar
COPY --from=build-stage /application/${JAR_FILE} fleet-management.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "fleet-management.jar"]
