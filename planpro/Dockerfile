# Build stage
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy gradle files first for better caching
COPY gradle gradle/
COPY gradlew build.gradle settings.gradle ./

# Download dependencies (this will be cached)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src src/

# Build the application
RUN gradle build --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Create uploads directory
RUN mkdir -p /tmp/uploads

# Copy the built jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port (Railway will set PORT environment variable)
EXPOSE 9090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${PORT:-9090}/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]