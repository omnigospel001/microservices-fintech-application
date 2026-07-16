# Multi-stage parameterized Dockerfile for any Spring Boot service
# Usage: docker build --build-arg SERVICE_NAME=<folder-name> -t <image-tag> .
ARG SERVICE_NAME

# ========== STAGE 1: Build ==========
FROM maven:3.9-eclipse-temurin-21 AS builder
ARG SERVICE_NAME
WORKDIR /build

# Copy the whole service module and build
COPY ${SERVICE_NAME} .
RUN mvn clean package -B -DskipTests

# ========== STAGE 2: Runtime ==========
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app

# Copy the generated JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
