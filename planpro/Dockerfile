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
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]