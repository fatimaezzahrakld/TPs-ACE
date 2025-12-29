FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/grpc-api-1.0.0.jar app.jar

EXPOSE 8082 9090

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
