#!/bin/bash
set -e

echo "========================================"
echo "Building all FinTech images for minikube"
echo "========================================"

# Point shell to minikube's Docker daemon
echo "Switching to minikube Docker daemon..."
eval $(minikube docker-env)

SERVICES=(
  config-server
  discovery-server
  gateway-server
  user-service
  deposit-service
  withdrawal-service
  transfer-service
  notification-service
  settlement-service
)

for svc in "${SERVICES[@]}"; do
  echo ""
  echo "----------------------------------------"
  echo "Building image: fintech-microservices-${svc}:latest"
  echo "----------------------------------------"
  docker build --build-arg SERVICE_NAME="${svc}" -t "fintech-microservices-${svc}:latest" ..
done

echo ""
echo "========================================"
echo "All images built successfully!"
echo "========================================"
