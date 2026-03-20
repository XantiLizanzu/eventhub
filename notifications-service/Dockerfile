# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /notifications-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn package -DskipTests

# Stage 2: Runtime image
FROM maven:3.9.6-amazoncorretto-21
WORKDIR /notifications-service
#COPY --from=build /app/target/*.jar app.jar
COPY --from=build /notifications-service/pom.xml pom.xml
COPY src src
EXPOSE 8082

# Run using Maven for hot reload support via mounted /notifications-service/target
ENTRYPOINT ["mvn", "spring-boot:run"]
