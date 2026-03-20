# Running development

Run a single project:

```bash
> cd <project-name>
> ./mvnw spring-boot:run
```

Run all projects using Docker compose:
```bash
> docker-compose up
```

For development purposes, hot reload is enabled. When making changes, save the file and build the project (`F9`) again. 
To make it work with Docker, the `target` folders are added as volumes.

# Todo's

- [x] Create 3 Maven projects with Spring Initializr
- [x] Add hello world endpoints
- [x] Add hot reload
- [x] Add Dockerfiles with the build and run phase
- [x] Add a Docker compose file with volumes