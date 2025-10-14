# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-slim
WORKDIR /app

# Create a non-root user
RUN useradd -m -u 1000 spring

# Switch to spring user
USER spring

COPY --from=build /app/target/*.jar app.jar

# The port your application listens on
ENV PORT=8080
ENV SERVER_PORT=${PORT}

# Run the application with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT} -Dspring.profiles.active=prod -jar app.jar"]
