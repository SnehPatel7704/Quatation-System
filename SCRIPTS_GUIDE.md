# Scripts Guide

This document explains all the startup and utility scripts available in the project.

## 📁 Available Scripts

### Setup Scripts (Run Once)

| Script | Platform | Purpose |
|--------|----------|---------|
| `setup.sh` | macOS/Linux | Initial project setup, checks prerequisites, installs dependencies |
| `setup.bat` | Windows | Initial project setup, checks prerequisites, installs dependencies |

### Startup Scripts

| Script | Platform | Purpose |
|--------|----------|---------|
| `start-all.sh` | macOS/Linux | Starts both backend and frontend together |
| `start-all.bat` | Windows | Starts both backend and frontend in separate windows |
| `start-backend.sh` | macOS/Linux | Starts only the backend server |
| `start-backend.bat` | Windows | Starts only the backend server |
| `start-frontend.sh` | macOS/Linux | Starts only the frontend server |
| `start-frontend.bat` | Windows | Starts only the frontend server |

## 🚀 Usage Guide

### First Time Setup

**macOS/Linux:**
```bash
# 1. Make setup script executable
chmod +x setup.sh

# 2. Run setup
./setup.sh

# This will:
# - Make all scripts executable
# - Check if Java, Node.js, MySQL are installed
# - Optionally install frontend dependencies
# - Optionally build the backend
```

**Windows:**
```cmd
# 1. Run setup
setup.bat

# This will:
# - Check if Java, Node.js, MySQL are installed
# - Optionally install frontend dependencies
# - Optionally build the backend
```

### Starting the Application

#### Option 1: Start Everything Together (Recommended)

**macOS/Linux:**
```bash
./start-all.sh
```
- Starts backend and frontend in the same terminal
- Shows both PIDs
- Logs to `backend.log` and `frontend.log`
- Press `Ctrl+C` to stop both servers

**Windows:**
```cmd
start-all.bat
```
- Opens 2 separate command prompt windows
- One for backend, one for frontend
- Close windows to stop servers

#### Option 2: Start Separately

**Backend Only:**

macOS/Linux:
```bash
./start-backend.sh
```

Windows:
```cmd
start-backend.bat
```

**Frontend Only:**

macOS/Linux:
```bash
./start-frontend.sh
```

Windows:
```cmd
start-frontend.bat
```

## 📝 Script Details

### setup.sh / setup.bat

**What it does:**
1. Checks if Java is installed (required: 17+)
2. Checks if Node.js is installed (required: 16+)
3. Checks if MySQL is installed (required: 8.0+)
4. Makes shell scripts executable (macOS/Linux only)
5. Optionally installs frontend dependencies (`npm install`)
6. Optionally builds backend (`mvn clean install`)

**When to use:**
- First time setting up the project
- After cloning the repository
- When switching between machines

### start-all.sh / start-all.bat

**What it does:**
- Starts Spring Boot backend on port 8080
- Starts React frontend on port 3000
- Handles both processes

**macOS/Linux specific:**
- Runs both in same terminal
- Creates log files: `backend.log` and `frontend.log`
- Cleanup on Ctrl+C

**Windows specific:**
- Opens 2 separate command prompt windows
- Each window shows its own logs
- Close windows individually to stop

**When to use:**
- Daily development
- Quick testing
- Demo purposes

### start-backend.sh / start-backend.bat

**What it does:**
1. Checks if Java is installed
2. Checks if Maven wrapper exists
3. Starts Spring Boot application
4. Shows startup logs in terminal

**Runs on:** `http://localhost:8080`

**When to use:**
- Backend-only development
- API testing
- When frontend is not needed

### start-frontend.sh / start-frontend.bat

**What it does:**
1. Checks if Node.js is installed
2. Navigates to frontend directory
3. Installs dependencies if needed (first run)
4. Starts React development server
5. Opens browser automatically

**Runs on:** `http://localhost:3000`

**When to use:**
- Frontend-only development
- UI/UX work
- When backend is already running

## 🔧 Customization

### Changing Ports

**Backend Port:**

Edit `src/main/resources/application.properties`:
```properties
server.port=8080  # Change to desired port
```

**Frontend Port:**

macOS/Linux:
```bash
PORT=3001 npm start
```

Windows:
```cmd
set PORT=3001 && npm start
```

Or create `.env` file in `frontend/`:
```
PORT=3001
```

### Adding Environment Variables

**macOS/Linux:**

Edit the startup script and add before the start command:
```bash
export DB_PASSWORD=mypassword
export MAIL_PASSWORD=myemailpass
```

**Windows:**

Edit the startup script and add before the start command:
```cmd
set DB_PASSWORD=mypassword
set MAIL_PASSWORD=myemailpass
```

## 🐛 Troubleshooting

### Scripts won't run (macOS/Linux)

**Problem:** Permission denied

**Solution:**
```bash
chmod +x setup.sh
chmod +x start-all.sh
chmod +x start-backend.sh
chmod +x start-frontend.sh
```

### Port already in use

**Problem:** Port 8080 or 3000 is already in use

**Solution:**

macOS/Linux:
```bash
# Find process on port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

Windows:
```cmd
# Find process on port 8080
netstat -ano | findstr :8080

# Kill process
taskkill /PID <PID> /F
```

### Backend won't start

**Check:**
1. Java version: `java -version` (need 17+)
2. MySQL is running
3. Database credentials in `application.properties`
4. Port 8080 is available

### Frontend won't start

**Check:**
1. Node.js version: `node -v` (need 16+)
2. Dependencies installed: `npm install`
3. Port 3000 is available
4. Backend is running (for API calls)

### Scripts not found (Windows)

**Problem:** 'start-all.bat' is not recognized

**Solution:**
- Make sure you're in the project root directory
- Use full path: `C:\path\to\project\start-all.bat`

## 📊 Monitoring

### View Logs

**macOS/Linux (when using start-all.sh):**
```bash
# Backend logs
tail -f backend.log

# Frontend logs
tail -f frontend.log

# Both logs
tail -f backend.log frontend.log
```

**Windows:**
- Logs appear in the command prompt windows
- Or redirect to files:
  ```cmd
  mvnw.cmd spring-boot:run > backend.log 2>&1
  ```

### Check if servers are running

**Backend:**
```bash
curl http://localhost:8080/api/auth/login
```

**Frontend:**
```bash
curl http://localhost:3000
```

Or open in browser:
- Backend: http://localhost:8080
- Frontend: http://localhost:3000

## 🎯 Best Practices

1. **Always run setup.sh/setup.bat first** on a new machine
2. **Use start-all** for quick development
3. **Use separate scripts** when working on specific parts
4. **Check logs** if something doesn't work
5. **Stop servers properly** (Ctrl+C or close windows)
6. **Update dependencies** regularly:
   ```bash
   # Backend
   ./mvnw clean install -U
   
   # Frontend
   cd frontend && npm update
   ```

## 🔄 Updating the Project

After pulling new changes:

**macOS/Linux:**
```bash
# Update backend dependencies
./mvnw clean install

# Update frontend dependencies
cd frontend
npm install
cd ..

# Restart servers
./start-all.sh
```

**Windows:**
```cmd
# Update backend dependencies
mvnw.cmd clean install

# Update frontend dependencies
cd frontend
npm install
cd ..

# Restart servers
start-all.bat
```

## 📞 Getting Help

If scripts don't work:

1. Check [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) for prerequisites
2. Check [README.md](README.md) for detailed setup
3. Review error messages in terminal/command prompt
4. Check log files (backend.log, frontend.log)
5. Verify all prerequisites are installed correctly
