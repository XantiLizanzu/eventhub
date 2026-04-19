# Stage 1: Build with Maven
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java (non-root)
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/users-service-*.jar app.jar
USER 1001
ENTRYPOINT ["java", "-jar", "app.jar"]
