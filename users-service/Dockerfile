# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /users-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn package -DskipTests

# Stage 2: Runtime image
FROM maven:3.9.6-amazoncorretto-21
WORKDIR /users-service
#COPY --from=build /users-service/target/*.jar app.jar
COPY --from=build /users-service/pom.xml pom.xml
COPY src src
EXPOSE 8085

# Run using Maven for hot reload support via mounted /users-service/target
ENTRYPOINT ["mvn", "spring-boot:run"]
