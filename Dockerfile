FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/botinfo-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
CMD ["java", "-jar", "app.jar"]
