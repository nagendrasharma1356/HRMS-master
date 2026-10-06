@echo off
echo Starting build and deployment process...

:: List of all services with exact folder names
set SERVICES=Api-Gateway Authentication-Service Client-Service Coupon-Service EventManagement Expense-Service Holiday-Service Lead-Service Leave-Management Notice-service Payroll-service Plan-Service Purchase-Service RoleAndPermissionService Service-Registry User-Service

:: Build and run each service
for %%s in (%SERVICES%) do (
    echo Building %%s...
    cd %%s
    call mvn clean package -DskipTests -Pprod
    if errorlevel 1 (
        echo Failed to build %%s
        pause
        exit /b 1
    )

    :: Extract jar name from target directory (in case of different versions)
    for /f "tokens=*" %%f in ('dir /b /a-d target\*.jar ^| findstr /v "original"') do (
        set JAR_FILE=%%f
    )

    start java -jar target/!JAR_FILE! --spring.profiles.active=prod
    cd ..
    timeout /t 5
)

echo All services have been built and started with production profile
pause