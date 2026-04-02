# ========== Stage 1: Build ==========
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ========== Stage 2: Run ==========
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the port (Railway sets PORT env var)
EXPOSE ${PORT:-8080}

# Run the application
ENTRYPOINT java -jar -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prod app.jar
