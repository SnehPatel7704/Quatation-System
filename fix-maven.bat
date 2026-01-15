@echo off
echo ==========================================
echo   Maven Wrapper Fix Script
echo ==========================================
echo.

REM Create directory
echo Creating .mvn\wrapper directory...
if not exist ".mvn\wrapper" mkdir .mvn\wrapper

REM Download wrapper JAR
echo Downloading Maven Wrapper JAR...
curl -L -o .mvn\wrapper\maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

if %errorlevel% neq 0 (
    echo X Failed to download Maven Wrapper JAR
    echo.
    echo Please check your internet connection or download manually from:
    echo https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
    echo.
    echo Save it to: .mvn\wrapper\maven-wrapper.jar
    pause
    exit /b 1
)

REM Check if file exists
if exist ".mvn\wrapper\maven-wrapper.jar" (
    echo + Maven Wrapper JAR downloaded successfully
) else (
    echo X Maven Wrapper JAR not found after download
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   + Maven Wrapper Fixed!
echo ==========================================
echo.
echo Test it with: mvnw.cmd -v
echo.
echo Start backend with: mvnw.cmd spring-boot:run
echo.

pause
