# 🏗️ Use the official JDK image
FROM openjdk:17-jdk-slim


# 📁 Set working directory in container
WORKDIR /app

# 🧾 Copy the JAR file into the container
COPY gateway-0.0.1-SNAPSHOT.jar app.jar

# 🚪 Expose the port used by the app
EXPOSE 9090

# 🚀 Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
