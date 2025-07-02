@echo off
REM Set JAVA_HOME for Maven
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.6.7-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
cd backend
call mvnw.cmd spring-boot:run -Ddebug > ..\backend_debug.log 2>&1
