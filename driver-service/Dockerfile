FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/driver-service-0.0.1-SNAPSHOT.jar /app/driver-service.jar

ENTRYPOINT ["java", "-jar", "driver-service.jar"]