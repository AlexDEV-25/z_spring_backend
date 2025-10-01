@echo off
echo Stopping any existing Spring Boot application...
taskkill /f /im java.exe 2>nul

echo Starting Spring Boot application...
cd /d "d:\khen\z_btl"
start "Backend Server" cmd /k "mvn spring-boot:run"

echo Backend is starting...
echo Check http://localhost:8080/actuator/health to verify it's running
pause
