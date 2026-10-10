# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven dependencies descriptor
COPY pom.xml .

# Download dependencies offline (layer cached)
RUN mvn dependency:go-offline -B

# Copy project source code
COPY src ./src

# Build production JAR (skip tests during container build)
RUN mvn clean package -DskipTests

# Stage 2: Production runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create and switch to non-root user for security best practices
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy packaged JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot server port (matches server.port=8081)
EXPOSE 8081

# Launch the Spring Boot application with container-aware JVM settings
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
