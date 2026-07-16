# Deploy FinTech Microservices to K8s (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Deploying FinTech Microservices to K8s" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

kubectl apply -f 00-namespace.yml
Write-Host "Namespace created" -ForegroundColor Green

kubectl apply -f 01-secrets.yml
Write-Host "Secrets created" -ForegroundColor Green

kubectl apply -f 02-configmap-postgres-init.yml
Write-Host "Postgres init ConfigMap created" -ForegroundColor Green

kubectl apply -f 10-postgresql.yml
Write-Host "PostgreSQL deployed" -ForegroundColor Green

kubectl apply -f 11-mongodb.yml
Write-Host "MongoDB deployed" -ForegroundColor Green

kubectl apply -f 12-kafka.yml
Write-Host "Kafka deployed" -ForegroundColor Green

kubectl apply -f 13-zipkin.yml
Write-Host "Zipkin deployed" -ForegroundColor Green

kubectl apply -f 14-pgadmin.yml
Write-Host "pgAdmin deployed" -ForegroundColor Green

kubectl apply -f 15-mongo-express.yml
Write-Host "Mongo Express deployed" -ForegroundColor Green

Write-Host ""
Write-Host "Waiting 30 seconds for infrastructure to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

kubectl apply -f 20-config-server.yml
Write-Host "Config Server deployed" -ForegroundColor Green

kubectl apply -f 21-discovery-server.yml
Write-Host "Discovery Server deployed" -ForegroundColor Green

kubectl apply -f 22-gateway-server.yml
Write-Host "Gateway Server deployed" -ForegroundColor Green

Write-Host ""
Write-Host "Waiting 40 seconds for platform services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 40

kubectl apply -f 30-user-service.yml
Write-Host "User Service deployed" -ForegroundColor Green

kubectl apply -f 31-deposit-service.yml
Write-Host "Deposit Service deployed" -ForegroundColor Green

kubectl apply -f 32-withdrawal-service.yml
Write-Host "Withdrawal Service deployed" -ForegroundColor Green

kubectl apply -f 33-transfer-service.yml
Write-Host "Transfer Service deployed" -ForegroundColor Green

kubectl apply -f 34-notification-service.yml
Write-Host "Notification Service deployed" -ForegroundColor Green

kubectl apply -f 35-settlement-service.yml
Write-Host "Settlement Service deployed" -ForegroundColor Green

kubectl apply -f 40-ingress.yml
Write-Host "Ingress deployed" -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Deployment complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
$minikubeIp = minikube ip
Write-Host "Add the following to your hosts file (C:\Windows\System32\drivers\etc\hosts as Administrator):" -ForegroundColor Yellow
Write-Host "  ${minikubeIp} fintech.local" -ForegroundColor White
Write-Host ""
Write-Host "Access points:" -ForegroundColor Green
Write-Host "  Gateway (via Ingress): http://fintech.local"
Write-Host "  pgAdmin:               minikube service pgadmin -n fintech"
Write-Host "  Mongo Express:         minikube service mongo-express -n fintech"
Write-Host ""
Write-Host "Watch pods:" -ForegroundColor Green
Write-Host "  kubectl get pods -n fintech -w"
