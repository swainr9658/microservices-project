# -------- Build stage --------
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Copy everything
COPY . .

# Give execute permission to gradlew (Linux requirement)
RUN chmod +x gradlew

# Build Spring Boot jar
RUN ./gradlew clean bootJar -x test

# -------- Runtime stage --------
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]