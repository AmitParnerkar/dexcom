# Use a base image with Java 17 or whatever version you're using
FROM amazoncorretto:17

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file (built from Maven or Gradle) into the container
COPY build/libs/dexcom-ai-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application's default port (change this if necessary)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]