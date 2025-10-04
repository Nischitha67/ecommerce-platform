# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar (after mvn package)
COPY target/api-gateway-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
