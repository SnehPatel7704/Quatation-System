#!/bin/bash

echo "=========================================="
echo "  Maven Wrapper Fix Script"
echo "=========================================="
echo ""

# Create directory
echo "📁 Creating .mvn/wrapper directory..."
mkdir -p .mvn/wrapper

# Download wrapper JAR
echo "📦 Downloading Maven Wrapper JAR..."
if command -v curl &> /dev/null; then
    curl -L -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
elif command -v wget &> /dev/null; then
    wget -O .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
else
    echo "❌ Neither curl nor wget found. Please install one of them."
    exit 1
fi

# Check if download was successful
if [ -f ".mvn/wrapper/maven-wrapper.jar" ]; then
    echo "✓ Maven Wrapper JAR downloaded successfully"
else
    echo "❌ Failed to download Maven Wrapper JAR"
    exit 1
fi

# Make mvnw executable
echo "🔧 Making mvnw executable..."
chmod +x mvnw

echo ""
echo "=========================================="
echo "  ✓ Maven Wrapper Fixed!"
echo "=========================================="
echo ""
echo "Test it with: ./mvnw -v"
echo ""
echo "Start backend with: ./mvnw spring-boot:run"
echo ""
