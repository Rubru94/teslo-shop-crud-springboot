#####################################
# JAR needs to be built beforehand. #
#####################################

# Step 1: Use an official OpenJDK base image from Docker Hub
# FROM openjdk:17-jdk-alpine
FROM amazoncorretto:21

RUN echo "Dockerfile - JAR needs to be built beforehand."

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: set the JAR file path
ARG JAR_FILE=target/*.jar

# Step 4: Copy the Spring Boot JAR file, .env & application.yml into the container
COPY ${JAR_FILE} /app/teslo-shop-app.jar
# .env & application.yaml included to collect the app environment variables and thus avoid overwriting all the values in compose and having them duplicated.
COPY .env /app/.env
COPY src/main/resources/application.yaml /app/application.yaml

# Step 5: Expose the port your application runs on
EXPOSE 8085

# Step 6: Define the command to run your Spring Boot application
CMD ["java", "-Dspring.config.location=/app/application.yaml", "-jar", "/app/teslo-shop-app.jar"]