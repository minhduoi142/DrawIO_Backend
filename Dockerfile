
FROM maven:3.9.6-eclipse-temurin-22 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:22-jdk
WORKDIR /app

COPY --from=builder /app/target/DrIO-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]