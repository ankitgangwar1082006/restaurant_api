
FROM eclipse-temurin:21-jdk-alpine

VOLUME /tmp

COPY target/restaurant-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Step 5: Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]