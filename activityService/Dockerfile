# Use lightweight Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app


COPY activityService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your service runs on
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
