@echo off
for %%p in (3000 3001 3002 3003 3004 3005) do (
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%p ^| findstr LISTENING') do (
        echo Killing process on port %%p with PID %%a
        taskkill /PID %%a /F >nul 2>&1
    )
)
cd frontend
npm start
