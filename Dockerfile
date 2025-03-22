# Stage 1: Build the application
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the pom.xml first to leverage caching
COPY pom.xml .

# Download dependencies (cached if pom.xml hasn't changed)
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:21-alpine
WORKDIR /app

# Copy the built JAR from the build stage using a wildcard
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application will run on
EXPOSE 8181

# Set the entrypoint for the application
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}"]
