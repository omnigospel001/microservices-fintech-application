# Delete all FinTech K8s resources (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Deleting all FinTech K8s resources" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$files = @(
    "40-ingress.yml",
    "35-settlement-service.yml",
    "34-notification-service.yml",
    "33-transfer-service.yml",
    "32-withdrawal-service.yml",
    "31-deposit-service.yml",
    "30-user-service.yml",
    "22-gateway-server.yml",
    "21-discovery-server.yml",
    "20-config-server.yml",
    "15-mongo-express.yml",
    "14-pgadmin.yml",
    "13-zipkin.yml",
    "12-kafka.yml",
    "11-mongodb.yml",
    "10-postgresql.yml",
    "02-configmap-postgres-init.yml",
    "01-secrets.yml",
    "00-namespace.yml"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        kubectl delete -f $file --ignore-not-found=true 2>$null
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All resources deleted." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
