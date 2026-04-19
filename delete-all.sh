#!/bin/bash

# delete all Kubernetes resources for the SeOA Project


echo "deleting events-service..."
kubectl delete -f ./events-service/ -n eventhub

echo "deleting notifications-service..."
kubectl delete -f ./notifications-service/ -n eventhub

echo "deleting users-service..."
kubectl delete -f ./users-service/ -n eventhub

echo "deleting ticketing-service..."
kubectl delete -f ./ticketing-service/ -n eventhub

echo "deleting payments-service..."
kubectl delete -f ./payments-service/ -n eventhub

echo "deleting swagger-ui..."
kubectl delete -f ./swagger-ui-service/ -n eventhub

echo "deleting istio-virtual-service..."
kubectl delete -f ./istio-virtual-service.yaml -n eventhub

echo "All services deployed successfully!"

# Verify deployments
kubectl get deployments -n eventhub
kubectl get services -n eventhub
kubectl get pods -n eventhub