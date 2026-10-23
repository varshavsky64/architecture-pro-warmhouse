# Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Download dependencies first, so they are cached between builds
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copy the source code and build the jar
COPY src ./src
RUN mvn -B -q package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/target/devices-service.jar app.jar

# Expose the port the app runs on
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
