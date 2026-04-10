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

# Running on Kubernetes

## Prerequisites
- Install [Minikube](https://minikube.sigs.k8s.io/docs/start/) or another Kubernetes cluster.
- Install [kubectl](https://kubernetes.io/docs/tasks/tools/).
- Install [Istio](https://istio.io/latest/docs/setup/getting-started/).
- Install [Helm](https://helm.sh/docs/intro/install/) for Kiali installation.

## Steps to Set Up the Cluster

1. **Start Minikube**:
   ```bash
   minikube start
   ```

2. **Enable Istio in Minikube**:
   ```bash
   minikube addons enable istio
   minikube addons enable istio-provisioner
   ```

3. **Build Docker Images**:
   ```bash
   docker compose build
   ```

4. **Load Images into Minikube**:
   ```bash
   minikube image load events-service:latest
   minikube image load notifications-service:latest
   minikube image load ticketing-service:latest
   minikube image load payments-service:latest
   minikube image load users-service:latest
   ```

5. **Apply Kubernetes Deployments**:
   ```bash
   kubectl apply -f kubernetes/
   ```

6. **Apply Istio Gateway**:
   ```bash
   kubectl apply -f kubernetes/istio-gateway.yaml
   ```

7. **Install Kiali (Optional but recommended)**:
   ```bash
   helm repo add kiali https://kiali.org/helm-charts
   helm repo update
   helm install kiali kiali/kiali-server --namespace istio-system --set auth.strategy=anonymous
   ```

8. **Access Services**:
   - Get the Minikube IP:
     ```bash
     minikube ip
     ```
   - Access the services using the Minikube IP and the appropriate ports as defined in the Istio Gateway.

## Verifying the Setup

- Check the status of the pods:
  ```bash
  kubectl get pods
  ```

- Check the status of the services:
  ```bash
  kubectl get services
  ```

- Check the Istio Gateway:
  ```bash
  kubectl get gateway
  ```

- Check the VirtualService:
  ```bash
  kubectl get virtualservice
  ```

## Accessing Kiali Dashboard

Kiali has been installed to visualize your Istio service mesh. To access it:

1. **Port-forward Kiali service**:
   ```bash
   kubectl port-forward svc/kiali 20001:20001 -n istio-system
   ```

2. **Access Kiali in your browser**:
   Open [http://localhost:20001](http://localhost:20001) in your web browser.

3. **Explore your service mesh**:
   - View the graph of your services and their connections
   - Check metrics and health of your services
   - Monitor traffic flow between microservices
   - Troubleshoot issues in your mesh

## Kiali Features for First-Time Users

- **Graph View**: Visual representation of your service mesh topology
- **Service Details**: Metrics, health, and configuration for each service
- **Workloads**: Individual pod-level metrics and details
- **Istio Config**: View and validate your Istio configuration objects
- **Traffic Management**: Understand how traffic flows through your mesh

To stop the port-forwarding, press `Ctrl+C` in your terminal.
