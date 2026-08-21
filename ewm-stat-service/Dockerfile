FROM amazoncorretto:21-alpine-jdk
LABEL authors="burdak"
COPY target/*.jar stat-service.jar

ENTRYPOINT ["java", "-jar", "stat-service.jar"]
