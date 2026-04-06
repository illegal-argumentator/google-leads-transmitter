#!/bin/bash

DOCKER_PATH=/opt/introlab/alex-melnyk/google-leads-transmitter/docker/docker-compose.yml

echo "Pulling latest code..."
git pull

echo "Rebuilding and starting container..."
docker-compose -f "$DOCKER_PATH" up -d --build google-leads-app

echo "Waiting for service to start..."

for i in {1..30}; do

  if curl -s http://localhost:8140/api/actuator/health | grep '"status":"UP"' > /dev/null; then
    echo "Service is UP!"
    break
  fi

  echo "Waiting $i..."
  sleep 2

done