@echo off
REM Initial setup script for Windows

echo ==========================================
echo   Quotation System - Initial Setup
echo ==========================================
echo.

REM Check prerequisites
echo Checking prerequisites...
echo.

REM Check Java
java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo + Java is installed:
    java -version 2>&1 | findstr "version"
) else (
    echo X Java is NOT installed
    echo    Download from: https://adoptium.net/
)
echo.

REM Check Node.js
node -v >nul 2>&1
if %errorlevel% equ 0 (
    echo + Node.js is installed:
    node -v
) else (
    echo X Node.js is NOT installed
    echo    Download from: https://nodejs.org/
)
echo.

REM Check npm
npm -v >nul 2>&1
if %errorlevel% equ 0 (
    echo + npm is installed:
    npm -v
) else (
    echo X npm is NOT installed
)
echo.

REM Check MySQL
mysql --version >nul 2>&1
if %errorlevel% equ 0 (
    echo + MySQL is installed:
    mysql --version
) else (
    echo X MySQL is NOT installed
    echo    Download from: https://dev.mysql.com/downloads/installer/
)
echo.

REM Ask if user wants to install frontend dependencies
set /p INSTALL_DEPS="Install frontend dependencies now? (y/n): "
if /i "%INSTALL_DEPS%"=="y" (
    if exist "frontend" (
        echo Installing frontend dependencies...
        cd frontend
        call npm install
        cd ..
        echo + Frontend dependencies installed
    ) else (
        echo X Frontend directory not found
    )
)
echo.

REM Ask if user wants to build backend
set /p BUILD_BACKEND="Build backend now? (y/n): "
if /i "%BUILD_BACKEND%"=="y" (
    echo Building backend...
    call mvnw.cmd clean install -DskipTests
    echo + Backend built successfully
)
echo.

echo ==========================================
echo   + Setup Complete!
echo ==========================================
echo.
echo Next steps:
echo   1. Configure database in src\main\resources\application.properties
echo   2. Start the application:
echo      start-all.bat
echo.
echo   Or start separately:
echo      start-backend.bat
echo      start-frontend.bat
echo.

pause
