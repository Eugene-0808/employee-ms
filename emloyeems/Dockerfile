# Stage 1: Build the application using Maven
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Create the final runtime image
FROM eclipse-temurin:17-jdk-alpine

# Create a non-root user to run the app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]