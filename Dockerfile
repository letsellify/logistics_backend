# Stage 1: Build the application
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the pom.xml first (leveraging Docker layer caching)
COPY pom.xml .

# Download dependencies first to take advantage of caching
RUN mvn dependency:go-offline

# Copy the entire source code and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:21-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8181

# Run the application without specifying a profile
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
