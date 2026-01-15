#!/bin/bash

# Startup script for macOS/Linux

echo "=========================================="
echo "  Quotation System - Backend Startup"
echo "=========================================="
echo ""

# Set JAVA_HOME to Java 17 if available
if [ -d "/opt/homebrew/opt/openjdk@17" ]; then
    export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
    echo "✓ Using Java 17 from Homebrew"
elif [ -d "/usr/local/opt/openjdk@17" ]; then
    export JAVA_HOME=/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
    echo "✓ Using Java 17 from Homebrew"
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

echo "✓ Java version:"
java -version
echo ""

# Prefer Maven over Maven wrapper (more reliable)
if command -v mvn &> /dev/null; then
    echo "🚀 Starting Spring Boot backend using Maven..."
    echo "   Backend will be available at: http://localhost:8080"
    echo ""
    echo "   Press Ctrl+C to stop the server"
    echo ""
    mvn spring-boot:run
elif [ -f "./mvnw" ] && [ -f ".mvn/wrapper/maven-wrapper.jar" ]; then
    echo "🚀 Starting Spring Boot backend using Maven Wrapper..."
    echo "   Backend will be available at: http://localhost:8080"
    echo ""
    echo "   Press Ctrl+C to stop the server"
    echo ""
    chmod +x ./mvnw
    ./mvnw spring-boot:run
else
    echo "❌ Neither Maven nor Maven wrapper found."
    echo ""
    echo "Solutions:"
    echo "  1. Install Maven: brew install maven"
    echo "  2. Fix Maven wrapper: chmod +x fix-maven.sh && ./fix-maven.sh"
    echo "  3. See MAVEN_SETUP.md for detailed instructions"
    exit 1
fi
