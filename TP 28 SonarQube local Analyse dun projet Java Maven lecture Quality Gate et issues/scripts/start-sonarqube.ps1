# Create Docker volumes
docker volume create sonarqube_data
docker volume create sonarqube_logs
docker volume create sonarqube_extensions

# Pull latest image
docker pull sonarqube:community

# Start SonarQube
docker run -d --name sonarqube -p 9000:9000 `
  -v sonarqube_data:/opt/sonarqube/data `
  -v sonarqube_logs:/opt/sonarqube/logs `
  -v sonarqube_extensions:/opt/sonarqube/extensions `
  sonarqube:community

Write-Host "Waiting for SonarQube to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 60

# Check if SonarQube is responding
try {
    $response = Invoke-RestMethod -Uri "http://localhost:9000/api/system/status"
    Write-Host "SonarQube status: $($response.status)" -ForegroundColor Green
} catch {
    Write-Host "SonarQube is not ready yet, please wait..." -ForegroundColor Red
}

Write-Host "`nSonarQube is accessible at http://localhost:9000" -ForegroundColor Green
Write-Host "Login: admin" -ForegroundColor Cyan
Write-Host "Password: admin" -ForegroundColor Cyan
