
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/cs16bot-1.0-jar-with-dependencies.jar app.jar
CMD ["java", "-jar", "app.jar"]
