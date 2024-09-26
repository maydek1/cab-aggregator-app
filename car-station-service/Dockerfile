FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/car-station-service-0.0.1-SNAPSHOT.jar /app/car-station-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "car-station-service-0.0.1-SNAPSHOT.jar"]