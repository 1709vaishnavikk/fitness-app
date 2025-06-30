# -------- STAGE 1: Build the JAR --------
# Use an official Maven image with JDK 17 to compile your Spring Boot project
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project from your local machine to the container
COPY . .

# Build the Spring Boot project and skip tests for faster builds
RUN mvn clean package -DskipTests


# -------- STAGE 2: Create a Lightweight Runtime Image --------
# Use a small, secure JRE-only image based on Alpine Linux
FROM eclipse-temurin:17-jre-alpine

# Create a new user and group to avoid running the app as root (security best practice)
RUN addgroup -S spring && adduser -S spring -G spring

# Switch to the newly created non-root user
USER spring:spring

# Set the working directory where the app will run
WORKDIR /app

# Copy only the final JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
