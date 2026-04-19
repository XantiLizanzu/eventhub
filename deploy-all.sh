#!/bin/bash

# Apply all Kubernetes resources for the SeOA Project


echo "Deploying events-service..."
kubectl apply -f ./events-service/ -n eventhub

echo "Deploying notifications-service..."
kubectl apply -f ./notifications-service/ -n eventhub

echo "Deploying users-service..."
kubectl apply -f ./users-service/ -n eventhub

echo "Deploying ticketing-service..."
kubectl apply -f ./ticketing-service/ -n eventhub

echo "Deploying payments-service..."
kubectl apply -f ./payments-service/ -n eventhub

echo "Deploying swagger-ui..."
kubectl apply -f ./swagger-ui-service/ -n eventhub

echo "Deploying istio-virtual-service..."
kubectl apply -f ./istio-virtual-service.yaml -n eventhub

echo "All services deployed successfully!"

# Verify deployments
kubectl get deployments -n eventhub
kubectl get services -n eventhub
kubectl get pods -n eventhub