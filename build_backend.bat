@echo off
REM Set JAVA_HOME for Maven
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.6.7-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM Free up port 8080 (Windows)
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do (
    echo Killing process on port 8080 with PID %%a
    taskkill /PID %%a /F
)

cd backend
call mvnw.cmd clean package

REM Start the backend server
call mvnw.cmd spring-boot:run
