# Stage 1: Build
FROM maven:3.9-eclipse-temurin-23-alpine AS builder
WORKDIR /app
COPY src src
COPY pom.xml pom.xml
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/auth-nexus-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
