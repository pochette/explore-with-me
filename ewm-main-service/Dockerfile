FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY ewm-stats-client/pom.xml ewm-stats-client/pom.xml
COPY ewm-main-service/pom.xml ewm-main-service/pom.xml
COPY ewm-stat-service/pom.xml ewm-stat-service/pom.xml

RUN mvn -pl ewm-main-service -am dependency:go-offline

COPY ewm-stats-client/src ewm-stats-client/src
COPY ewm-main-service/src ewm-main-service/src

RUN mvn -pl ewm-main-service -am package -DskipTests

FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY --from=builder /app/ewm-main-service/target/*.jar main-service.jar

ENTRYPOINT ["java", "-jar", "main-service.jar"]