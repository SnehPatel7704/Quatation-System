# 🚀 Get Started in 5 Minutes

Welcome! This guide will get you up and running quickly.

## ✅ Prerequisites Check

Before starting, make sure you have:

- [ ] **Java 17+** installed
- [ ] **Node.js 16+** installed  
- [ ] **MySQL 8.0+** installed and running
- [ ] **Git** (if cloning from repository)

**Quick check:**
```bash
# macOS/Linux
java -version && node -v && mysql --version

# Windows
java -version && node -v && mysql --version
```

If any are missing, see [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) for installation instructions.

---

## 🎯 Step-by-Step Setup

### Step 1: Get the Project

```bash
# If using Git
git clone <repository-url>
cd quotation-system

# Or download and extract the ZIP file
```

### Step 2: Configure Database & Secrets

1. Make sure MySQL is running:
   ```bash
   # macOS/Linux
   brew services list | grep mysql
   
   # Windows
   net start | findstr MySQL
   ```

2. Create a `.env` file at project root (copy from `.env.example`) and set values. Do NOT commit this file.

3. Edit `src/main/resources/application.properties` to read credentials from environment variables (example shown):
   ```properties
   spring.datasource.username=${DB_USERNAME:root}
   spring.datasource.password=${DB_PASSWORD}
   ```

4. Provide a base64 `JWT_SECRET` to persist token signing across restarts. See `README.md` for details.

### Step 3: Run Setup Script

**macOS/Linux:**
```bash
chmod +x setup.sh
./setup.sh
```

**Windows:**
```cmd
setup.bat
```

This will:
- ✅ Check if all prerequisites are installed
- ✅ Make scripts executable (macOS/Linux)
- ✅ Install frontend dependencies
- ✅ Build the backend

### Step 4: Start the Application

**macOS/Linux:**
```bash
./start-all.sh
```

**Windows:**
```cmd
start-all.bat
```

Wait for both servers to start (about 30-60 seconds).

### Step 5: Access the Application

1. Open your browser
2. Go to: **http://localhost:3000**
3. Login with any of these accounts:

**📋 See [CREDENTIALS.md](CREDENTIALS.md) for complete details**

| Role | Username | Password |
|------|----------|----------|
| Super Admin | `spadmin` | `pass` |
| Admin | `admin` | `password` |
| User | `user` | `password` |

---

## 🎉 You're Ready!

### What to do next:

1. **Explore the Dashboard**
   - See your role and permissions
   - Navigate through different sections

2. **Try the Theme Toggle**
   - Click the sun/moon icon in the header
   - Switch between light and dark mode

3. **Create Your First User** (as Super Admin)
   - Go to "Users" in the navigation
   - Click "Add User"
   - Create an Admin or User account

4. **Add Companies and Products**
   - Navigate to "Companies" and add a client
   - Navigate to "Products" and add products

5. **Create a Quotation**
   - Go to "Quotations"
   - Click "Create Quotation"
   - Select company and add products

---

## 📱 Application Overview

### Navigation

```
┌─────────────────────────────────────────────────┐
│  Quotation System    [Dashboard] [Quotations]   │
│                      [Companies] [Products]      │
│                      [Users]     [Theme] [Logout]│
└─────────────────────────────────────────────────┘
```

### User Roles

| Role | Can Do |
|------|--------|
| **SUPERADMIN** | Everything + User Management |
| **ADMIN** | Create/Edit Quotations, Manage Companies/Products |
| **USER** | View Only |

---

## 🔧 Daily Usage

### Starting the Application

**Option 1: Start Everything (Recommended)**
```bash
# macOS/Linux
./start-all.sh

# Windows
start-all.bat
```

**Option 2: Start Separately**
```bash
# Terminal/CMD 1 - Backend
./start-backend.sh    # macOS/Linux
start-backend.bat     # Windows

# Terminal/CMD 2 - Frontend
./start-frontend.sh   # macOS/Linux
start-frontend.bat    # Windows
```

### Stopping the Application

**macOS/Linux:**
- Press `Ctrl+C` in the terminal

**Windows:**
- Close the command prompt windows

---

## 🐛 Common Issues

### "Port 8080 already in use"

**Solution:**
```bash
# macOS/Linux
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### "Cannot connect to database"

**Solution:**
1. Check MySQL is running
2. Verify username/password in `application.properties`
3. Try: `mysql -u root -p` to test connection

### "npm install fails"

**Solution:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### "Scripts not executable" (macOS/Linux)

**Solution:**
```bash
chmod +x setup.sh start-all.sh start-backend.sh start-frontend.sh
```

---

## 📚 Learn More

| Document | What's Inside |
|----------|---------------|
| [QUICK_REFERENCE.md](QUICK_REFERENCE.md) | Quick commands and tips |
| [README.md](README.md) | Complete documentation |
| [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md) | Architecture details |
| [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) | Detailed setup guide |
| [SCRIPTS_GUIDE.md](SCRIPTS_GUIDE.md) | All about the scripts |

---

## 💡 Pro Tips

1. **Bookmark the frontend URL:** http://localhost:3000
2. **Use the startup scripts** for quick development
3. **Check logs** if something doesn't work:
   - Backend: Terminal output or `backend.log`
   - Frontend: Browser console (F12) or `frontend.log`
4. **Theme preference is saved** - it persists across sessions
5. **JWT tokens expire after 24 hours** - just login again

---

## 🎯 Quick Commands Cheat Sheet

```bash
# macOS/Linux
./setup.sh              # First time setup
./start-all.sh          # Start everything
./start-backend.sh      # Backend only
./start-frontend.sh     # Frontend only

# Windows
setup.bat               # First time setup
start-all.bat           # Start everything
start-backend.bat       # Backend only
start-frontend.bat      # Frontend only
```

---

## ✨ Features to Explore

- ✅ **Role-Based Access** - Different views for different roles
- ✅ **Dark/Light Theme** - Toggle in header
- ✅ **Responsive Design** - Works on mobile too
- ✅ **Real-time Updates** - See changes immediately
- ✅ **Email Notifications** - Configure SMTP for emails
- ✅ **Secure Authentication** - JWT-based security

---

## 🆘 Need Help?

1. **Check the documentation** - Start with [QUICK_REFERENCE.md](QUICK_REFERENCE.md)
2. **Review error messages** - They usually tell you what's wrong
3. **Check the logs** - Backend and frontend logs have details
4. **Verify prerequisites** - Make sure Java, Node, MySQL are installed

---

## 🎊 Congratulations!

You now have a fully functional quotation management system running!

**Next Steps:**
- Explore all the features
- Create test data
- Customize for your needs
- Read the full documentation

**Happy Quoting! 🚀**
