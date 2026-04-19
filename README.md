# 🚀 Starting Cluster

### Prerequisites
If not installed:
```bash
curl -L https://istio.io/downloadIstio | sh -
```

### Set up .env by copying .env.template
```bash
cp .env.template .env
```

### Start Minikube
```bash
minikube start --memory=8192mb --cpus=4
```

### Apply Namespace Configuration
```bash
kubectl apply -f Namespace_config.yaml
```

### Enable Istio Injection
```bash
kubectl label namespace eventhub istio-injection=enabled
```

### Install Istio
note: this takes quite some time you can continue in another terminal
```bash
istio-*/bin/istioctl install --set values.global.platform=minikube
```
Or, if you have Istioctl installed:
```bash
istioctl install --set values.global.platform=minikube
```

### Apply Istio Gateway
```bash
kubectl apply -f istio-gateway.yaml
```

### Configure Docker Environment
To let your local Docker daemon communicate with the Minikube Docker daemon:
```bash
eval $(minikube -p minikube docker-env)
```

### Build and Deploy
note: both building and actually deploying takes quite some time, you can see the deploying progress on the dashboard
```bash
docker compose build
./deploy-all.sh
```
### To check the dashboard (in another terminal)
```bash
minikube dashboard
```

### Port Forwarding
To access the services you will need to forward the port from the gateway.
```bash
kubectl port-forward -n istio-system svc/istio-ingressgateway 8080:80
```

## Access service

### Global swagger ui
note: you can switch service in the top right corner.

http://localhost:8080/swagger/

### open API specification
note that double naming in the address is necessary!(this was done to get events/events/swagger-ui/index.html to work)
http://localhost:8080/events/events/v3/api-docs

### Example of an api page
http://localhost:8080/events/events

# 🛠️ Running Development

First, rename `.env.template` to `.env` and update the values if necessary.

## Using Docker Compose
To run all services using Docker Compose, first build the images:

```bash
docker compose build
```

Then start up all containers:
```bash
docker compose up
```

For development purposes, hot reload is enabled. When making changes, save the file and build the project (`Ctrl+F9`) again.
To make it work with Docker, the `target` folders are added as volumes.

## Run a Single Project
This is for development purposes only.
```bash
cd <project-name>
./mvnw spring-boot:run
```

# 🚀 Running Production

## Using Docker Compose
To run all services using Docker Compose, first build the images:
```bash
docker compose -f docker-compose.prod.yml build
```

Then start up all containers:
```bash
docker compose up
```


## SELinux Permission Issues
Label directories as container_file:
```bash
sudo chcon -Rt container_file_t ./*/target
```

**Verify:**
```bash
ls -Z ./events-service/target
```

# 🔧 IntelliJ Development

If you are using IntelliJ, you might need to add the different services as modules. To do so, press `Ctrl+Shift+Alt+S` to open the project structure dialog. Then go to `Modules > + > Import Module` and import every service as a Maven project.