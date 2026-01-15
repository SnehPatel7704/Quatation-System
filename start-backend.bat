@echo off
REM Startup script for Windows

echo ==========================================
echo   Quotation System - Backend Startup
echo ==========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo X Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

echo + Java version:
java -version
echo.

REM Check for Maven or Maven wrapper
if exist "mvnw.cmd" if exist ".mvn\wrapper\maven-wrapper.jar" (
    echo Starting Spring Boot backend using Maven Wrapper...
    echo    Backend will be available at: http://localhost:8080
    echo.
    echo    Press Ctrl+C to stop the server
    echo.
    call mvnw.cmd spring-boot:run
) else (
    where mvn >nul 2>&1
    if %errorlevel% equ 0 (
        echo Starting Spring Boot backend using Maven...
        echo    Backend will be available at: http://localhost:8080
        echo.
        echo    Press Ctrl+C to stop the server
        echo.
        call mvn spring-boot:run
    ) else (
        echo X Neither Maven wrapper nor Maven found.
        echo.
        echo Solutions:
        echo   1. Install Maven from: https://maven.apache.org/download.cgi
        echo   2. Fix Maven wrapper: fix-maven.bat
        echo   3. See MAVEN_SETUP.md for detailed instructions
        pause
        exit /b 1
    )
)
