# ---------- Build stage ----------
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy the source code and the Gradle files
COPY build.gradle* settings.gradle* ./
COPY src ./src

# Grant execute permission for the Gradle Wrapper (if you use it)
# RUN chmod +x gradlew

# Build the project and create the JAR, skipping tests
# Use ./gradlew if you use the wrapper, or just 'gradle' if you don't.
RUN gradle clean build -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the builder stage
# The JAR path might vary. Check your build.gradle to see the 'libs' directory name.
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]