# --- Stage 1: The "Build" Stage ---
# This stage builds our .jar file using Maven

# Use an official "builder" image that has Maven and Java (JDK 17)
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download all the dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of our source code
COPY src ./src

# Build the application! This runs our unit tests
# and creates the final .jar file.
RUN ./mvnw package -DskipTests

# --- Stage 2: The "Run" Stage ---
# This stage builds the final, small image that will *run* our app

# Use a small, secure "runtime" image that only has Java (JRE 17)
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy *only* the built .jar file from the "builder" stage
COPY --from=builder /app/target/product-api-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port our app runs on
EXPOSE 8080

# This is the "Start Command"
# It tells Render how to run our app
ENTRYPOINT ["java", "-jar", "./app.jar"]