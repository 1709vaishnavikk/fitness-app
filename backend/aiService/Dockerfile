# Use lightweight JDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy the jar into the container
COPY aiService-0.0.1-SNAPSHOT.jar app.jar

# Expose app port (example 8083)
EXPOSE 8083

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
