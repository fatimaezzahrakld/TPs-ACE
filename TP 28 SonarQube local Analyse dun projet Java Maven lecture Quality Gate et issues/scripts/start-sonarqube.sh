#!/usr/bin/env bash
set -euo pipefail

# Create Docker volumes
docker volume create sonarqube_data
docker volume create sonarqube_logs
docker volume create sonarqube_extensions

# Pull latest image
docker pull sonarqube:community

# Start SonarQube
docker run -d --name sonarqube -p 9000:9000 \
  -v sonarqube_data:/opt/sonarqube/data \
  -v sonarqube_logs:/opt/sonarqube/logs \
  -v sonarqube_extensions:/opt/sonarqube/extensions \
  sonarqube:community

# Wait for SonarQube to start (may take 1-2 min)
echo "Waiting for SonarQube to be ready..."
sleep 60

# Check if SonarQube is responding
curl -s http://localhost:9000/api/system/status

echo "SonarQube is ready at http://localhost:9000"
echo "Login: admin"
echo "Password: admin"
