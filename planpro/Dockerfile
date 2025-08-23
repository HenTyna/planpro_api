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

# Create the directory your app actually uses (matches application.yml)
RUN mkdir -p /tmp/upload-files/temp_qr

# Copy the fat jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy startup script
COPY startup.sh /app/startup.sh
RUN chmod +x /app/startup.sh

# Do NOT rely on env in EXPOSE; it's a build-time hint only
EXPOSE 8080

# JVM tuned for Railway's memory constraints
# Railway typically provides 512MB-1GB RAM, so we need to be very conservative
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=50 -XX:MinRAMPercentage=25 -XX:+ExitOnOutOfMemoryError -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dfile.encoding=UTF-8 -Dspring.profiles.active=railway"

# Remove healthcheck for now - it can cause issues on Railway
# HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=5 \
#   CMD curl -fsS "http://localhost:${PORT:-8080}/actuator/health" | grep -q '"status":"UP"' || exit 1

# Run on the port Railway provides (fallback 8080 locally)
# Use startup script for better error handling
ENTRYPOINT ["/app/startup.sh"]