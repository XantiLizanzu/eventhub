#!/bin/bash

# Apply all Kubernetes resources for the SeOA Project

echo "Applying ConfigMap..."
kubectl apply -f ./configmap.yaml -n eventhub

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

echo "All services deployed successfully!"

# Verify deployments
kubectl get deployments -n eventhub
kubectl get services -n eventhub
kubectl get pods -n eventhub