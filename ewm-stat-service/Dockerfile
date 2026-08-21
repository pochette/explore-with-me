FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY ewm-stats-client/pom.xml ewm-stats-client/pom.xml
COPY ewm-main-service/pom.xml ewm-main-service/pom.xml
COPY ewm-stat-service/pom.xml ewm-stat-service/pom.xml

RUN mvn -pl ewm-stat-service -am dependency:go-offline

COPY ewm-stats-client/src ewm-stats-client/src
COPY ewm-stat-service/src ewm-stat-service/src

RUN mvn -pl ewm-stat-service -am package -DskipTests

FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY --from=builder /app/ewm-stat-service/target/*.jar stat-service.jar

ENTRYPOINT ["java", "-jar", "stat-service.jar"]