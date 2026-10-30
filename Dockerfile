# Estágio 1: Build da aplicação usando Maven e OpenJDK 8 (compatibilidade total)
FROM maven:3.8.8-openjdk-8-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Ambiente de execução leve com Java 8
FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
