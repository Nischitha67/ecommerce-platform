# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR into container
COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
