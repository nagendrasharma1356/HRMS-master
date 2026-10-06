#!/bin/bash

echo "Starting build and deployment process..."

# List of all services with exact folder names
SERVICES=(
    "Api-Gateway"
    "Authentication-Service"
    "Client-Service"
    "Coupon-Service"
    "EventManagement"
    "Expense-Service"
    "Holiday-Service"
    "Lead-Service"
    "Leave-Management"
    "Notice-service"
    "Payroll-service"
    "Plan-Service"
    "Purchase-Service"
    "RoleAndPermissionService"
    "Service-Registry"
    "User-Service"
)

# Build and run each service
for service in "${SERVICES[@]}"
do
    echo "Building $service..."
    cd "$service"

    # Build with Maven
    if ! mvn clean package -DskipTests -Pprod; then
        echo "Failed to build $service"
        exit 1
    fi

    # Find the JAR file (excluding original-*.jar files)
    JAR_FILE=$(ls target/*.jar | grep -v 'original')

    # Run the jar file
    nohup java -jar "$JAR_FILE" --spring.profiles.active=prod > "$service.log" 2>&1 &

    cd ..
    sleep 5

    echo "$service started with PID: $!"
done

echo "All services have been built and started with production profile"
echo "Check individual .log files for service outputs"