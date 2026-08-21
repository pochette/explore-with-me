FROM amazoncorretto:21-alpine-jdk
LABEL authors="burdak"
COPY target/*.jar main-service.jar


ENTRYPOINT ["java", "-jar", "main-service.jar"]
