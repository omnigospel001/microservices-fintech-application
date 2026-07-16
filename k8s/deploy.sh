#!/bin/bash
set -e

echo "========================================"
echo "Deploying FinTech Microservices to K8s"
echo "========================================"

kubectl apply -f 00-namespace.yml
echo "Namespace created"

kubectl apply -f 01-secrets.yml
echo "Secrets created"

kubectl apply -f 02-configmap-postgres-init.yml
echo "Postgres init ConfigMap created"

kubectl apply -f 10-postgresql.yml
echo "PostgreSQL deployed"

kubectl apply -f 11-mongodb.yml
echo "MongoDB deployed"

kubectl apply -f 12-kafka.yml
echo "Kafka deployed"

kubectl apply -f 13-zipkin.yml
echo "Zipkin deployed"

kubectl apply -f 14-pgadmin.yml
echo "pgAdmin deployed"

kubectl apply -f 15-mongo-express.yml
echo "Mongo Express deployed"

echo ""
echo "Waiting 30 seconds for infrastructure to be ready..."
sleep 30

kubectl apply -f 20-config-server.yml
echo "Config Server deployed"

kubectl apply -f 21-discovery-server.yml
echo "Discovery Server deployed"

kubectl apply -f 22-gateway-server.yml
echo "Gateway Server deployed"

echo ""
echo "Waiting 40 seconds for platform services to be ready..."
sleep 40

kubectl apply -f 30-user-service.yml
echo "User Service deployed"

kubectl apply -f 31-deposit-service.yml
echo "Deposit Service deployed"

kubectl apply -f 32-withdrawal-service.yml
echo "Withdrawal Service deployed"

kubectl apply -f 33-transfer-service.yml
echo "Transfer Service deployed"

kubectl apply -f 34-notification-service.yml
echo "Notification Service deployed"

kubectl apply -f 35-settlement-service.yml
echo "Settlement Service deployed"

kubectl apply -f 40-ingress.yml
echo "Ingress deployed"

echo ""
echo "========================================"
echo "Deployment complete!"
echo "========================================"
echo ""
echo "Add the following to /etc/hosts:"
echo "  $(minikube ip) fintech.local"
echo ""
echo "Access points:"
echo "  Gateway (via Ingress): http://fintech.local"
echo "  Eureka Dashboard:      http://$(minikube ip):$(kubectl get svc discovery-server -n fintech -o jsonpath='{.spec.ports[0].nodePort} 2>/dev/null || echo 8761')"
echo "  pgAdmin:               minikube service pgadmin -n fintech"
echo "  Mongo Express:         minikube service mongo-express -n fintech"
echo ""
echo "Watch pods:"
echo "  kubectl get pods -n fintech -w"
