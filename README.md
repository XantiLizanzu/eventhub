# Running development

First, rename `.env.template` to `.env` and update in the values if necessary.

## Using docker compose 
To run all services using Docker compose, first build the images:
```bash
docker compose build
```

Then start up all containers:
```bash
docker compose up
```

For development purposes, hot reload is enabled. When making changes, save the file and build the project (`Ctrl+F9`) again. 
To make it work with Docker, the `target` folders are added as volumes.

## Run a single project
This is for development purposes only.
```bash
cd <project-name>
./mvnw spring-boot:run
```

# Running production

## Using docker compose 
To run all services using Docker compose, first build the images:
```bash
docker compose -f docker-compose.prod.yml build
```

Then start up all containers:
```bash
docker compose up
```

## Environment variables

Rename `.env.template` to `.env` and update in the values if necessary.

## SELinux permission issues 
Label directories as container_file
`sudo chcon -Rt container_file_t ./*/target`

**# Verify**
ls -Z ./events-service/target

# IntelliJ development

If you are using IntelliJ, you might need to add the different services as modules. To do so, press `Ctrl+Shift+Alt+S`
to the project structure dialog. Then go to `Modules > + > Import Module` and import every service as a Maven project.
