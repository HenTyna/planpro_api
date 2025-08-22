# Build stage
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy the entire current directory (planpro contents)
COPY . .

# Make gradlew executable (important for Railway)
RUN chmod +x gradlew

# Download dependencies (this will be cached)
RUN ./gradlew dependencies --no-daemon

# Build the application (skip tests for faster build)
RUN ./gradlew build -x test --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install curl for health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create uploads directory
RUN mkdir -p /tmp/uploads

# Copy the built jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port (Railway will set PORT environment variable)
EXPOSE $PORT

# Health check using Railway's PORT environment variable
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

# Use Railway's PORT environment variable with memory optimization
ENTRYPOINT ["sh", "-c", "java -Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport -Dserver.port=${PORT:-8080} -jar app.jar"]