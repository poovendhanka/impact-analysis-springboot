# Multi-stage Production Dockerfile for Spring Boot Backend
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Cache maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build executable jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime Stage
FROM eclipse-temurin:21-jre-jammy

# Install curl for healthcheck probe
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Run as non-privileged service user
RUN groupadd -r spring && useradd -r -g spring -u 10002 -d /home/spring -m spring
WORKDIR /app

COPY --from=builder /build/target/impact-analysis-springboot-1.0.0.jar app.jar
RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=20s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
