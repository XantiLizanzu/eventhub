#!/bin/bash

# Delete all Kubernetes resources for the SeOA Project

# Delete Swagger UI
echo "Deleting swagger-ui..."
kubectl delete -f ./swagger-ui/ -n eventhub

echo "Deleting events-service..."
kubectl delete -f ./events-service/ -n eventhub

echo "Deleting notifications-service..."
kubectl delete -f ./notifications-service/ -n eventhub

echo "Deleting users-service..."
kubectl delete -f ./users-service/ -n eventhub

echo "Deleting ticketing-service..."
kubectl delete -f ./ticketing-service/ -n eventhub

echo "Deleting payments-service..."
kubectl delete -f ./payments-service/ -n eventhub

echo "All services deleted successfully!"

# Verify deletions
kubectl get deployments -n eventhub
kubectl get services -n eventhub
kubectl get pods -n eventhub