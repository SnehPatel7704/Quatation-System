@echo off
REM Startup script to run both backend and frontend (Windows)

echo ==========================================
echo   Quotation System - Full Stack Startup
echo ==========================================
echo.

REM Check if required commands exist
java -v >nul 2>&1
if %errorlevel% neq 0 (
    echo X Java is not installed.
    pause
    exit /b 1
)

node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo X Node.js is not installed.
    pause
    exit /b 1
)

echo + Prerequisites check passed
echo.

REM Start backend in new window
echo Starting backend server...
start "Quotation System - Backend" cmd /k "mvnw.cmd spring-boot:run"
echo    Backend will be available at: http://localhost:8080
echo.

REM Wait a bit for backend to start
timeout /t 5 /nobreak >nul

REM Start frontend in new window
echo Starting frontend server...
start "Quotation System - Frontend" cmd /k "cd frontend && npm install && npm start"
echo    Frontend will be available at: http://localhost:3000
echo.

echo ==========================================
echo   + Both servers are starting...
echo ==========================================
echo.
echo   Backend:  http://localhost:8080
echo   Frontend: http://localhost:3000
echo.
echo   Close the terminal windows to stop the servers
echo.

pause
