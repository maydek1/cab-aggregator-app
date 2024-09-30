FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/passenger-service-0.0.1-SNAPSHOT.jar /app/passenger-service.jar

ENTRYPOINT ["java", "-jar", "passenger-service.jar"]