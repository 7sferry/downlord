FROM eclipse-temurin:17-jdk-alpine AS build
LABEL authors="ferry"

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
EXPOSE 8765

ENTRYPOINT ["java","-jar","target/downlord-0.0.1-SNAPSHOT.jar"]
