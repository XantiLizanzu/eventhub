#!/bin/bash

# Test different paths to find what works with Istio Ingress

echo "=== Testing Istio Ingress Paths ==="
INGRESS_URL=http://192.168.49.2:32735

echo "Testing base URL: $INGRESS_URL"
curl -s -w "\nHTTP Status: %{http_code}\n" "$INGRESS_URL/" | head -1
echo ""

echo "Testing events service paths:"
echo "1. /events/v3/api-docs:"
curl -s -w "HTTP Status: %{http_code}\n" "$INGRESS_URL/events/v3/api-docs" | head -3
echo ""

echo "2. /v3/api-docs:"
curl -s -w "HTTP Status: %{http_code}\n" "$INGRESS_URL/v3/api-docs" | head -3
echo ""

echo "Testing Swagger UI paths:"
echo "1. /swagger-ui/index.html:"
curl -s -w "HTTP Status: %{http_code}\n" "$INGRESS_URL/swagger-ui/index.html" | head -5
echo ""

echo "2. /events/swagger-ui/index.html:"
curl -s -w "HTTP Status: %{http_code}\n" "$INGRESS_URL/events/swagger-ui/index.html" | head -5
echo ""

echo "Testing direct service access from within cluster:"
kubectl exec -n eventhub event-app-fcfc45bcb-kj82m -- curl -s -w "\nHTTP Status: %{http_code}\n" http://events-service:8080/swagger-ui/index.html | head -5
echo ""

echo "Testing API docs from within cluster:"
kubectl exec -n eventhub event-app-fcfc45bcb-kj82m -- curl -s -w "\nHTTP Status: %{http_code}\n" http://events-service:8080/v3/api-docs | head -3
echo ""

echo "=== Summary ==="
echo "If you see HTML output above, that path is working."
echo "If you see JSON with 'openapi' in it, the API docs are accessible."
echo "Use the working paths in your browser to access Swagger UI."