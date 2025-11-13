# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
ARG CACHE_BUST=1
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Bust cache when CACHE_BUST changes and update snapshots
RUN echo "Cache bust: ${CACHE_BUST}" && mvn -U clean package -DskipTests

# Run stage
FROM amazoncorretto:17-alpine
WORKDIR /app

# Create a non-root user
RUN adduser -D -u 1000 spring

# Switch to spring user
USER spring

COPY --from=build /app/target/*.jar app.jar

# The port your application listens on
ENV PORT=8080
ENV SERVER_PORT=${PORT}
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Run the application with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT} -Dspring.profiles.active=prod -jar app.jar"]