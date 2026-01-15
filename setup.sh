#!/bin/bash

# Initial setup script for macOS/Linux

echo "=========================================="
echo "  Quotation System - Initial Setup"
echo "=========================================="
echo ""

# Make all shell scripts executable
echo "📝 Making scripts executable..."
chmod +x start-backend.sh
chmod +x start-frontend.sh
chmod +x start-all.sh
chmod +x mvnw
echo "✓ Scripts are now executable"
echo ""

# Check prerequisites
echo "🔍 Checking prerequisites..."
echo ""

# Check Java
if command -v java &> /dev/null; then
    echo "✓ Java is installed:"
    java -version 2>&1 | head -n 1
else
    echo "❌ Java is NOT installed"
    echo "   Install: brew install openjdk@17"
fi
echo ""

# Check Node.js
if command -v node &> /dev/null; then
    echo "✓ Node.js is installed:"
    echo "   Version: $(node -v)"
else
    echo "❌ Node.js is NOT installed"
    echo "   Install: brew install node"
fi
echo ""

# Check npm
if command -v npm &> /dev/null; then
    echo "✓ npm is installed:"
    echo "   Version: $(npm -v)"
else
    echo "❌ npm is NOT installed"
fi
echo ""

# Check MySQL
if command -v mysql &> /dev/null; then
    echo "✓ MySQL is installed:"
    mysql --version
else
    echo "❌ MySQL is NOT installed"
    echo "   Install: brew install mysql"
    echo "   Start: brew services start mysql"
fi
echo ""

# Ask if user wants to install frontend dependencies
read -p "📦 Install frontend dependencies now? (y/n) " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    if [ -d "frontend" ]; then
        echo "Installing frontend dependencies..."
        cd frontend
        npm install
        cd ..
        echo "✓ Frontend dependencies installed"
    else
        echo "❌ Frontend directory not found"
    fi
fi
echo ""

# Ask if user wants to build backend
read -p "🔨 Build backend now? (y/n) " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Building backend..."
    ./mvnw clean install -DskipTests
    echo "✓ Backend built successfully"
fi
echo ""

echo "=========================================="
echo "  ✓ Setup Complete!"
echo "=========================================="
echo ""
echo "Next steps:"
echo "  1. Configure database in src/main/resources/application.properties"
echo "  2. Start the application:"
echo "     ./start-all.sh"
echo ""
echo "  Or start separately:"
echo "     ./start-backend.sh"
echo "     ./start-frontend.sh"
echo ""
