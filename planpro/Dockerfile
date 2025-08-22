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

# Optional: add curl only if you keep a healthcheck
RUN apk add --no-cache curl

# Copy the fat jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Do NOT rely on env in EXPOSE; it’s a build-time hint only
EXPOSE 8080

# JVM tuned for container limits (let the JVM size itself to a % of cgroup memory)
# Remove the hard -Xmx and use RAM percentages instead
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -XX:MinRAMPercentage=20 -XX:+ExitOnOutOfMemoryError -Dfile.encoding=UTF-8"

# If you actually have Spring Boot Actuator on the classpath, keep this.
# Give the app time to warm up (increase start-period); otherwise, comment out the HEALTHCHECK.
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=5 \
  CMD curl -fsS "http://localhost:${PORT:-8080}/actuator/health" | grep -q '"status":"UP"' || exit 1

# Run on the port Railway provides (fallback 8080 locally)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]

# End of Selection