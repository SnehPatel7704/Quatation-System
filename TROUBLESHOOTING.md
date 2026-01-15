# Troubleshooting Guide

## Maven Wrapper Issues

### Error: "Could not find or load main class org.apache.maven.wrapper.MavenWrapperMain"

**Quick Fix:**

**macOS/Linux:**
```bash
chmod +x fix-maven.sh
./fix-maven.sh
```

**Windows:**
```cmd
fix-maven.bat
```

**Or install Maven directly (recommended):**

macOS:
```bash
brew install maven
```

Windows:
- Download from: https://maven.apache.org/download.cgi
- Or: `choco install maven` (if you have Chocolatey)

Then use `mvn` instead of `./mvnw` or `mvnw.cmd`

**See [MAVEN_SETUP.md](MAVEN_SETUP.md) for detailed solutions**

---

## Port Already in Use

### Backend (Port 8080)

**macOS/Linux:**
```bash
# Find process
lsof -i :8080

# Kill process
kill -9 <PID>
```

**Windows:**
```cmd
# Find process
netstat -ano | findstr :8080

# Kill process
taskkill /PID <PID> /F
```

### Frontend (Port 3000)

**macOS/Linux:**
```bash
# Find process
lsof -i :3000

# Kill process
kill -9 <PID>
```

**Windows:**
```cmd
# Find process
netstat -ano | findstr :3000

# Kill process
taskkill /PID <PID> /F
```

---

## Database Connection Issues

### MySQL Not Running

**macOS:**
```bash
# Start MySQL
brew services start mysql

# Check status
brew services list | grep mysql
```

**Windows:**
```cmd
# Start MySQL
net start MySQL80

# Check status
sc query MySQL80
```

**Linux:**
```bash
# Start MySQL
sudo systemctl start mysql

# Check status
sudo systemctl status mysql
```

### Wrong Credentials

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Test Connection

```bash
mysql -u root -p
```

---

## Frontend Issues

### npm install fails

**Solution:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Module not found errors

**Solution:**
```bash
cd frontend
npm install
```

### Port 3000 in use

**Change port:**

macOS/Linux:
```bash
PORT=3001 npm start
```

Windows:
```cmd
set PORT=3001 && npm start
```

---

## Backend Issues

### Java version error

**Check version:**
```bash
java -version
```

**Need Java 17 or higher**

**Install:**
- macOS: `brew install openjdk@17`
- Windows: Download from https://adoptium.net/
- Linux: `sudo apt install openjdk-17-jdk`

### Build fails

**Clean and rebuild:**

macOS/Linux:
```bash
./mvnw clean install -U
# or
mvn clean install -U
```

Windows:
```cmd
mvnw.cmd clean install -U
REM or
mvn clean install -U
```

---

## Login Issues

### Cannot login

**Check:**
1. Backend is running (http://localhost:8080)
2. Database is running
3. Using correct credentials (see [CREDENTIALS.md](CREDENTIALS.md))
4. Browser console for errors (F12)

**Default credentials:**
- spadmin / pass
- admin / password
- user / password

### Token expired

**Solution:** Just login again (tokens expire after 24 hours)

---

## CORS Errors

### "Access-Control-Allow-Origin" error

**Check:**
1. Backend is running on port 8080
2. Frontend is running on port 3000
3. `frontend/package.json` has: `"proxy": "http://localhost:8080"`

---

## Email Not Sending

### SMTP Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

**For Gmail:**
1. Enable 2-Step Verification
2. Generate App Password: https://myaccount.google.com/apppasswords
3. Use app password (not regular password)

---

## Scripts Not Working

### Permission denied (macOS/Linux)

**Solution:**
```bash
chmod +x setup.sh start-all.sh start-backend.sh start-frontend.sh fix-maven.sh
```

### Script not found (Windows)

**Solution:**
- Make sure you're in the project root directory
- Use full path: `C:\path\to\project\start-all.bat`

---

## Theme Not Persisting

**Solution:**
1. Check browser localStorage is enabled
2. Clear browser cache
3. Try different browser

---

## General Debugging

### Check Backend Logs

**Console output** or check `backend.log` (if using start-all.sh)

### Check Frontend Logs

**Browser console (F12)** or check `frontend.log` (if using start-all.sh)

### Verify Services Running

**Backend:**
```bash
curl http://localhost:8080/api/auth/login
# Should return 400 or 405 (not connection error)
```

**Frontend:**
```bash
curl http://localhost:3000
# Should return HTML
```

---

## Still Having Issues?

1. **Check all prerequisites:**
   - Java 17+
   - Node.js 16+
   - MySQL 8.0+
   - Maven (optional but recommended)

2. **Review documentation:**
   - [GET_STARTED.md](GET_STARTED.md)
   - [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md)
   - [MAVEN_SETUP.md](MAVEN_SETUP.md)

3. **Clean start:**
   ```bash
   # Stop all servers
   # Delete node_modules
   # Delete target folder
   # Run setup again
   ```

4. **Check versions:**
   ```bash
   java -version    # Should be 17+
   node -v          # Should be 16+
   npm -v           # Should be 8+
   mysql --version  # Should be 8.0+
   ```

---

## Quick Fixes Summary

| Issue | Quick Fix |
|-------|-----------|
| Maven wrapper error | Run `fix-maven.sh` or `fix-maven.bat` |
| Port in use | Kill process using the port |
| Database error | Check MySQL is running |
| npm errors | Delete node_modules and reinstall |
| Login fails | Check backend is running |
| CORS error | Verify ports 8080 and 3000 |
| Scripts won't run | chmod +x (macOS/Linux) |

---

**For detailed solutions, see the specific documentation files mentioned above.**
