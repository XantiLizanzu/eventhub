FROM amazoncorretto:21 as builder
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM amazoncorretto:21
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8081