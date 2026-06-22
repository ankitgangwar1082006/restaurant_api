# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Pehle pom.xml copy karke dependencies download karo
COPY pom.xml .
COPY src ./src

# Application ko build karo (aur tests ko skip karo taaki jaldi ho)
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Run the Application
# ==========================================
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Stage 1 se bani hui jar file ko Stage 2 mein copy karo
COPY --from=build /app/target/restaurant-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]