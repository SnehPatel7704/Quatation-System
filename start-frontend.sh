#!/bin/bash

# Startup script for macOS/Linux

echo "=========================================="
echo "  Quotation System - Frontend Startup"
echo "=========================================="
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 16 or higher."
    exit 1
fi

echo "✓ Node.js version:"
node -v
echo ""

echo "✓ npm version:"
npm -v
echo ""

# Navigate to frontend directory
if [ ! -d "frontend" ]; then
    echo "❌ Frontend directory not found. Please ensure you're in the project root directory."
    exit 1
fi

cd frontend

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
    echo ""
fi

echo "🚀 Starting React development server..."
echo "   Frontend will be available at: http://localhost:3000"
echo ""
echo "   Press Ctrl+C to stop the server"
echo ""

# Start the application
npm start
