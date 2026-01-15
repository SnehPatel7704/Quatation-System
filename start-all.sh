#!/bin/bash

# Startup script to run both backend and frontend (macOS/Linux)

echo "=========================================="
echo "  Quotation System - Full Stack Startup"
echo "=========================================="
echo ""

# Check if required commands exist
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed."
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed."
    exit 1
fi

echo "✓ Prerequisites check passed"
echo ""

# Function to cleanup on exit
cleanup() {
    echo ""
    echo "🛑 Shutting down servers..."
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    exit
}

trap cleanup SIGINT SIGTERM

# Start backend
echo "🚀 Starting backend server..."
chmod +x ./mvnw
./mvnw spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "   Backend PID: $BACKEND_PID"
echo "   Backend logs: backend.log"

# Wait for backend to start
echo "   Waiting for backend to start..."
sleep 10

# Start frontend
echo ""
echo "🚀 Starting frontend server..."
cd frontend
npm install > /dev/null 2>&1
npm start > ../frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..
echo "   Frontend PID: $FRONTEND_PID"
echo "   Frontend logs: frontend.log"

echo ""
echo "=========================================="
echo "  ✓ Both servers are starting..."
echo "=========================================="
echo ""
echo "  Backend:  http://localhost:8080"
echo "  Frontend: http://localhost:3000"
echo ""
echo "  Press Ctrl+C to stop both servers"
echo ""
echo "  Logs:"
echo "    Backend:  tail -f backend.log"
echo "    Frontend: tail -f frontend.log"
echo ""

# Wait for both processes
wait $BACKEND_PID $FRONTEND_PID
