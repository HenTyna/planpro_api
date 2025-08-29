# ---------- Build stage ----------
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Cache Gradle deps better by copying metadata first
COPY gradle gradle
COPY gradlew settings.gradle build.gradle ./
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies || true

# Now bring in sources
COPY . .
RUN ./gradlew clean bootJar -x test --no-daemon

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Create the directory your app actually uses (matches application.yml)
RUN mkdir -p /tmp/upload-files/temp_qr

# Copy the fat jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy startup script
COPY startup.sh startup.sh
RUN chmod +x startup.sh

# Do NOT rely on env in EXPOSE; it's a build-time hint only
EXPOSE 8080

# JVM tuned for Railway's memory constraints
# Railway typically provides 512MB-1GB RAM, so we need to be conservative
# Note: JAVA_TOOL_OPTIONS will be set by startup.sh

# Set default port for Railway
ENV PORT=8080

# Healthcheck for Railway
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

# Run on the port Railway provides (fallback 8080 locally)
# Use exec form for better signal handling
ENTRYPOINT ["./startup.sh"]