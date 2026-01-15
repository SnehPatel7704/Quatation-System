@echo off
REM Startup script for Windows

echo ==========================================
echo   Quotation System - Frontend Startup
echo ==========================================
echo.

REM Check if Node.js is installed
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo X Node.js is not installed. Please install Node.js 16 or higher.
    pause
    exit /b 1
)

echo + Node.js version:
node -v
echo.

echo + npm version:
npm -v
echo.

REM Navigate to frontend directory
if not exist "frontend" (
    echo X Frontend directory not found. Please ensure you're in the project root directory.
    pause
    exit /b 1
)

cd frontend

REM Check if node_modules exists
if not exist "node_modules" (
    echo Installing dependencies...
    call npm install
    echo.
)

echo Starting React development server...
echo    Frontend will be available at: http://localhost:3000
echo.
echo    Press Ctrl+C to stop the server
echo.

REM Start the application
call npm start
