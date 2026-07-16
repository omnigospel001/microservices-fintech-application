#!/bin/bash
set -e

echo "========================================"
echo "Deleting all FinTech K8s resources"
echo "========================================"

kubectl delete -f 40-ingress.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 35-settlement-service.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 34-notification-service.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 33-transfer-service.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 32-withdrawal-service.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 31-deposit-service.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 30-user-service.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 22-gateway-server.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 21-discovery-server.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 20-config-server.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 15-mongo-express.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 14-pgadmin.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 13-zipkin.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 12-kafka.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 11-mongodb.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 10-postgresql.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 02-configmap-postgres-init.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 01-secrets.yml --ignore-not-found=true 2>/dev/null || true
kubectl delete -f 00-namespace.yml --ignore-not-found=true 2>/dev/null || true

echo ""
echo "========================================"
echo "All resources deleted."
echo "========================================"
