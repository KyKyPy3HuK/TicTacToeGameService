# Этап build
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Сборка
RUN mvn clean package

# Этап запуска
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/TicTacToeGame-0.0.1-SNAPSHOT.jar app.jar

# Порт
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]