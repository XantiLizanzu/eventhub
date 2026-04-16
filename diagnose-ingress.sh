#!/bin/bash

# Comprehensive Istio Ingress Diagnosis Script
# Tests all components needed for Swagger UI access via Istio Ingress

echo "=== Istio Ingress Gateway Diagnosis ==="
echo "Testing all components for Swagger UI access"
echo ""

# Test 1: Check if Istio ingress gateway pod is running
echo "Test 1: Istio Ingress Gateway Pod Status"
INGRESS_POD=$(kubectl get pods -n istio-system -l istio=ingressgateway -o name 2>/dev/null | head -1)
if [ -n "$INGRESS_POD" ]; then
    INGRESS_STATUS=$(kubectl get pod -n istio-system $INGRESS_POD -o jsonpath='{.status.phase}')
    INGRESS_READY=$(kubectl get pod -n istio-system $INGRESS_POD -o jsonpath='{.status.containerStatuses[0].ready}')
    echo "✓ Ingress gateway pod found: $INGRESS_POD"
    echo "  Status: $INGRESS_STATUS"
    echo "  Ready: $INGRESS_READY"
else
    echo "✗ FAIL: No ingress gateway pod found"
fi
echo ""

# Test 2: Check if Istio ingress gateway service exists
echo "Test 2: Istio Ingress Gateway Service"
INGRESS_SERVICE=$(kubectl get svc -n istio-system istio-ingressgateway -o jsonpath='{.metadata.name}' 2>/dev/null)
if [ -n "$INGRESS_SERVICE" ]; then
    INGRESS_TYPE=$(kubectl get svc -n istio-system istio-ingressgateway -o jsonpath='{.spec.type}')
    INGRESS_PORTS=$(kubectl get svc -n istio-system istio-ingressgateway -o jsonpath='{.spec.ports[*].port}')
    echo "✓ Ingress gateway service found"
    echo "  Type: $INGRESS_TYPE"
    echo "  Ports: $INGRESS_PORTS"
else
    echo "✗ FAIL: No ingress gateway service found"
fi
echo ""

# Test 3: Check if Gateway resource exists
echo "Test 3: Istio Gateway Resource"
GATEWAY=$(kubectl get gateway -n eventhub seoa-gateway -o jsonpath='{.metadata.name}' 2>/dev/null)
if [ -n "$GATEWAY" ]; then
    GATEWAY_HOSTS=$(kubectl get gateway -n eventhub seoa-gateway -o jsonpath='{.spec.servers[0].hosts[0]}')
    GATEWAY_PORT=$(kubectl get gateway -n eventhub seoa-gateway -o jsonpath='{.spec.servers[0].port.number}')
    echo "✓ Gateway resource found: $GATEWAY"
    echo "  Hosts: $GATEWAY_HOSTS"
    echo "  Port: $GATEWAY_PORT"
else
    echo "✗ FAIL: Gateway resource not found"
fi
echo ""

# Test 4: Check if VirtualService resources exist
echo "Test 4: VirtualService Resources"
VIRTUALSERVICES=$(kubectl get virtualservice -n eventhub -o jsonpath='{.items[*].metadata.name}' 2>/dev/null)
if [ -n "$VIRTUALSERVICES" ]; then
    echo "✓ VirtualServices found:"
    for vs in $VIRTUALSERVICES; do
        MATCH=$(kubectl get virtualservice -n eventhub $vs -o jsonpath='{.spec.http[0].match[0].uri.prefix}')
        DEST=$(kubectl get virtualservice -n eventhub $vs -o jsonpath='{.spec.http[0].route[0].destination.host}')
        echo "  - $vs: $MATCH → $DEST"
    done
else
    echo "✗ FAIL: No VirtualServices found"
fi
echo ""

# Test 5: Check if services are running
echo "Test 5: Application Services Status"
SERVICES=("events-service" "users-service" "notifications-service" "ticketing-service" "payments-service")
for service in "${SERVICES[@]}"; do
    PODS=$(kubectl get pods -n eventhub -l app=${service%%-service}-app -o name 2>/dev/null | wc -l)
    if [ $PODS -gt 0 ]; then
        READY_PODS=$(kubectl get pods -n eventhub -l app=${service%%-service}-app -o jsonpath='{.items[*].status.containerStatuses[?(@.ready==true)].container}' | wc -w)
        echo "✓ $service: $PODS pods, $READY_PODS ready"
    else
        echo "✗ $service: No pods found"
    fi
done
echo ""

# Test 6: Check service endpoints
echo "Test 6: Service Endpoints"
for service in "${SERVICES[@]}"; do
    ENDPOINTS=$(kubectl get endpoints -n eventhub $service -o jsonpath='{.subsets[0].addresses[*].ip}' 2>/dev/null)
    if [ -n "$ENDPOINTS" ]; then
        echo "✓ $service endpoints: $ENDPOINTS"
    else
        echo "✗ $service: No endpoints found"
    fi
done
echo ""

# Test 7: Test direct service access from within cluster
echo "Test 7: Internal Service Connectivity"
EVENTS_POD=$(kubectl get pods -n eventhub -l app=Event_app -o name | head -1 | cut -d'/' -f2)
if [ -n "$EVENTS_POD" ]; then
    echo "Testing from events pod: $EVENTS_POD"
    
    # Test DNS resolution
    if kubectl exec -n eventhub $EVENTS_POD -- sh -c "getent hosts events-service.eventhub.svc.cluster.local" &>/dev/null; then
        echo "✓ DNS resolution working"
    else
        echo "✗ DNS resolution failed"
    fi
    
    # Test service connectivity
    HTTP_CODE=$(kubectl exec -n eventhub $EVENTS_POD -- sh -c "curl -s -o /dev/null -w '%{http_code}' http://events-service:8080/v3/api-docs" 2>/dev/null || echo "000")
    if [ "$HTTP_CODE" = "200" ]; then
        echo "✓ Internal API access working (HTTP $HTTP_CODE)"
    else
        echo "✗ Internal API access failed (HTTP $HTTP_CODE)"
    fi
else
    echo "✗ No events pod available for testing"
fi
echo ""

# Test 8: Get Minikube service URL
echo "Test 8: Minikube Service URL"
if kubectl get svc -n istio-system istio-ingressgateway &>/dev/null; then
    URL=$(minikube service -n istio-system istio-ingressgateway --url 2>/dev/null | head -1)
    if [ -n "$URL" ]; then
        echo "✓ Ingress gateway URL: $URL"
        
        # Test if URL is accessible
        echo "Testing URL accessibility..."
        HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$URL" 2>/dev/null || echo "000")
        if [ "$HTTP_CODE" = "404" ]; then
            echo "✓ URL accessible (404 means gateway is responding but no matching route)"
        elif [ "$HTTP_CODE" -ge "200" ] && [ "$HTTP_CODE" -lt "500" ]; then
            echo "✓ URL accessible (HTTP $HTTP_CODE)"
        else
            echo "✗ URL not accessible (HTTP $HTTP_CODE)"
        fi
        
        # Test specific service paths
        echo "Testing service paths..."
        for service in "events" "users" "notifications" "ticketing" "payments"; do
            SERVICE_URL="${URL}/${service}/v3/api-docs"
            HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$SERVICE_URL" 2>/dev/null || echo "000")
            if [ "$HTTP_CODE" = "200" ]; then
                echo "✓ ${service} service: $SERVICE_URL (HTTP $HTTP_CODE)"
            else
                echo "✗ ${service} service: $SERVICE_URL (HTTP $HTTP_CODE)"
            fi
        done
        
        # Provide Swagger UI URLs
        echo ""
        echo "=== Swagger UI Access URLs ==="
        for service in "events" "users" "notifications" "ticketing" "payments"; do
            echo "${service}: ${URL}/${service}/swagger-ui/index.html"
        done
    else
        echo "✗ Could not get Minikube service URL"
    fi
else
    echo "✗ Istio ingress gateway service not found"
fi
echo ""

echo "=== Diagnosis Complete ==="
echo ""
echo "Summary of what should be working:"
echo "1. Istio ingress gateway pod running"
echo "2. Gateway and VirtualService resources applied"
echo "3. Application services running and healthy"
echo "4. Service endpoints configured correctly"
echo "5. Internal service communication working"
echo ""
echo "If any tests failed above, those components need attention."
echo "The most common issues are:"
echo "- Missing ingress gateway pod (install with: istioctl install --set profile=empty --set components.ingressGateways.enabled=true -y)"
echo "- Missing Gateway/VirtualService resources (apply the YAML files)"
echo "- Services not ready (check logs with: kubectl logs -n eventhub <pod-name>)"
echo "- Path mismatches in VirtualService rules (check the prefix paths)"