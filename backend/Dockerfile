# Backend multi-stage Dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and source
COPY backend/pom.xml ./
COPY backend/src ./src

# Package the application (skip tests in container build)
RUN mvn clean package -DskipTests -q

# Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/enviro365-backend-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
