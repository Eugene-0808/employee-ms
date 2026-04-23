#!/bin/bash

echo "🚀 Starting deployment of the Employee Management System staging environment..."

# Stop and remove any existing containers, networks, and volumes
echo "🧹 Cleaning up previous deployment (if any)..."
docker-compose down -v

# Build the Docker images and start the services in detached mode
echo "🏗️  Building Docker images and starting services..."
docker-compose up -d --build

# Wait for services to be fully ready
echo "⏳ Waiting for services to be ready..."
sleep 15

# Display the status of the running containers
echo "✅ Deployment complete! Container status:"
docker-compose ps

echo ""
echo "🎉 The Employee Management System is now running in a staging environment!"
echo "📡 Access the API at: http://localhost:8080/api/v1/employee"
echo "🗄️  MySQL is accessible on port 3307."
echo ""
echo "To stop the environment, run: docker-compose down"