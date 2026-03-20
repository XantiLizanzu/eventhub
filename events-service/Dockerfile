# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /events-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn package -DskipTests

# Stage 2: Runtime image
FROM maven:3.9.6-amazoncorretto-21
WORKDIR /events-service
#COPY --from=build /events-service/target/*.jar app.jar
COPY --from=build /events-service/pom.xml pom.xml
COPY src src
EXPOSE 8081

# Run using Maven for hot reload support via mounted /events-service/target
ENTRYPOINT ["mvn", "spring-boot:run"]
