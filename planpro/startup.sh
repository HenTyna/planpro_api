#!/bin/bash

# Railway startup script
echo "Starting PlanPro API on Railway..."

# Set default values for Railway environment
export PORT=${PORT:-8080}
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-railway}

# Ensure required directories exist
mkdir -p /tmp/upload-files/temp_qr

# Set JVM options for Railway's memory constraints
export JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=40 -XX:MinRAMPercentage=20 -XX:+ExitOnOutOfMemoryError -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Dfile.encoding=UTF-8 -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"

echo "JVM Options: $JAVA_TOOL_OPTIONS"
echo "Server Port: $PORT"
echo "Spring Profile: $SPRING_PROFILES_ACTIVE"

# Start the application
exec java -Dserver.port=$PORT -jar app.jar
