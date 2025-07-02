@echo off
REM Automatically set JAVA_HOME for Maven
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.6.7-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
call kill_ports.bat
cd backend
call mvnw.cmd spring-boot:run
