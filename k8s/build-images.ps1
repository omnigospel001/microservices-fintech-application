# Build all FinTech images for minikube (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Building all FinTech images for minikube" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Point shell to minikube's Docker daemon
Write-Host "Switching to minikube Docker daemon..." -ForegroundColor Yellow
& minikube docker-env | Invoke-Expression

$services = @(
  "config-server",
  "discovery-server",
  "gateway-server",
  "user-service",
  "deposit-service",
  "withdrawal-service",
  "transfer-service",
  "notification-service",
  "settlement-service"
)

foreach ($svc in $services) {
  Write-Host ""
  Write-Host "----------------------------------------" -ForegroundColor Green
  Write-Host "Building image: fintech-microservices-${svc}:latest" -ForegroundColor Green
  Write-Host "----------------------------------------" -ForegroundColor Green
  docker build --build-arg SERVICE_NAME="${svc}" -t "fintech-microservices-${svc}:latest" ..
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All images built successfully!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
