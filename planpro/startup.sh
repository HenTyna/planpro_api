#!/bin/sh

# Railway startup script for Java application
echo "Starting PlanPro API on Railway..."

# Set default port if not provided
PORT=${PORT:-8080}
echo "Using port: $PORT"

# Set JVM options for Railway's memory constraints
export JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=50 -XX:MinRAMPercentage=25 -XX:+ExitOnOutOfMemoryError -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dfile.encoding=UTF-8 -Dspring.profiles.active=railway"

# Create required directories
mkdir -p /tmp/upload-files/temp_qr

# Start the application
echo "Starting Java application..."
exec java -Dserver.port=$PORT -jar app.jar
