# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /ticketing-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn package -DskipTests

# Stage 2: Runtime image
FROM maven:3.9.6-amazoncorretto-21
WORKDIR /ticketing-service
#COPY --from=build /app/target/*.jar app.jar
COPY --from=build /ticketing-service/pom.xml pom.xml
COPY src src
EXPOSE 8083

# Run using Maven for hot reload support via mounted /ticketing-service/target
ENTRYPOINT ["mvn", "spring-boot:run"]
