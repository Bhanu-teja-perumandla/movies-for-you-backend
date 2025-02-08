# Use an official Amazon Corretto runtime as a parent image
FROM amazoncorretto:21

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY build/libs/*.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]