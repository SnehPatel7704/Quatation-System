# Quick Reference Card

## 🚀 Getting Started (First Time)

### macOS/Linux
```bash
chmod +x setup.sh && ./setup.sh
./start-all.sh
```

### Windows
```cmd
setup.bat
start-all.bat
```

**Then open:** http://localhost:3000

**Login:** `spadmin` / `pass`

---

## 📝 Common Commands

### Start Application

| Action | macOS/Linux | Windows |
|--------|-------------|---------|
| Start both | `./start-all.sh` | `start-all.bat` |
| Backend only | `./start-backend.sh` | `start-backend.bat` |
| Frontend only | `./start-frontend.sh` | `start-frontend.bat` |

### Stop Application

| Platform | Command |
|----------|---------|
| macOS/Linux | Press `Ctrl+C` |
| Windows | Close command prompt windows |

### Build & Install

| Action | macOS/Linux | Windows |
|--------|-------------|---------|
| Backend build | `./mvnw clean install` | `mvnw.cmd clean install` |
| Frontend install | `cd frontend && npm install` | `cd frontend && npm install` |
| Clean build | `./mvnw clean package` | `mvnw.cmd clean package` |

---

## 🔧 Configuration Files

| File | Purpose |
|------|---------|
| `src/main/resources/application.properties` | Backend config (DB, email) |
| `frontend/package.json` | Frontend dependencies |
| `frontend/tailwind.config.js` | Tailwind CSS config |

---

## 🌐 URLs

| Service | URL | Default Port |
|---------|-----|--------------|
| Frontend | http://localhost:3000 | 3000 |
| Backend API | http://localhost:8080 | 8080 |
| MySQL | localhost | 3306 |

---

## 👥 Default Users

**📋 Complete credentials: [CREDENTIALS.md](CREDENTIALS.md)**

| Username | Password | Role | Access |
|----------|----------|------|--------|
| spadmin | pass | SUPERADMIN | Full access |
| admin | password | ADMIN | Create/edit quotations |
| user | password | USER | View only |

---

## 📁 Important Directories

| Path | Contains |
|------|----------|
| `src/main/java/com/quotation/` | Backend Java code |
| `src/main/resources/` | Config files, SQL |
| `frontend/src/` | React frontend code |
| `frontend/src/pages/` | Page components |
| `frontend/src/services/` | API services |

---

## 🔑 API Endpoints

### Authentication
```
POST /api/auth/login
GET  /api/auth/me
```

### Quotations
```
GET    /api/quotations
POST   /api/quotations
PUT    /api/quotations/{id}
DELETE /api/quotations/{id}
```

### Users (Super Admin)
```
GET    /api/superadmin/users
POST   /api/superadmin/users
DELETE /api/superadmin/users/{id}
```

### Companies & Products
```
GET /api/companies
GET /api/products
```

---

## 🐛 Quick Troubleshooting

### Port Already in Use

**macOS/Linux:**
```bash
# Kill process on port 8080
lsof -i :8080
kill -9 <PID>

# Kill process on port 3000
lsof -i :3000
kill -9 <PID>
```

**Windows:**
```cmd
# Kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Kill process on port 3000
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

### Database Connection Error

1. Check MySQL is running
2. Verify credentials in `application.properties`
3. Test connection: `mysql -u root -p`

### Frontend Won't Start

```bash
# Clear and reinstall
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Backend Won't Start

```bash
# Clean rebuild
./mvnw clean install -U
```

---

## 🎨 Theme Toggle

- **Location:** Header (sun/moon icon)
- **Shortcut:** Click icon to toggle
- **Persistence:** Saved in browser localStorage

---

## 📊 Database Tables

| Table | Purpose |
|-------|---------|
| users | System users |
| companies | Client companies |
| products | Product catalog |
| quotations | Quotation headers |
| quotation_items | Quotation line items |
| quotation_templates | Custom templates |

---

## 🔐 Security

- **Authentication:** JWT tokens
- **Token Expiry:** 24 hours
- **Password:** BCrypt encrypted
- **CORS:** Enabled for localhost:3000

---

## 📦 Dependencies

### Backend
- Spring Boot 3.2.1
- Spring Security 6.x
- MySQL Connector
- JWT 0.11.5
- Lombok

### Frontend
- React 18.2.0
- React Router 6.20.0
- Tailwind CSS 3.3.6
- Axios 1.6.2
- React Icons 4.12.0

---

## 🛠️ Development Tools

| Tool | Check Version |
|------|---------------|
| Java | `java -version` |
| Node.js | `node -v` |
| npm | `npm -v` |
| MySQL | `mysql --version` |
| Maven | `mvn -v` |

---

## 📝 Logs

### View Logs (macOS/Linux with start-all.sh)
```bash
tail -f backend.log
tail -f frontend.log
```

### View Logs (Windows)
- Check command prompt windows
- Or redirect: `mvnw.cmd spring-boot:run > backend.log 2>&1`

---

## 🔄 Update Project

```bash
# Pull latest changes
git pull

# Update backend
./mvnw clean install

# Update frontend
cd frontend
npm install
cd ..

# Restart
./start-all.sh
```

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| README.md | Main documentation |
| PROJECT_OVERVIEW.md | Architecture & structure |
| SETUP_CHECKLIST.md | Setup guide |
| SCRIPTS_GUIDE.md | Scripts documentation |
| QUICK_REFERENCE.md | This file |

---

## 💡 Tips

1. **First time?** Run `setup.sh` or `setup.bat`
2. **Daily use?** Use `start-all` scripts
3. **Backend only?** Use `start-backend` scripts
4. **Frontend only?** Use `start-frontend` scripts
5. **Issues?** Check logs and documentation

---

## 🎯 Common Tasks

### Create New User (Super Admin)
1. Login as spadmin
2. Navigate to Users
3. Click "Add User"
4. Fill form and select role
5. Save

### Create Quotation (Admin)
1. Navigate to Quotations
2. Click "Create Quotation"
3. Select company
4. Add products and quantities
5. Save

### Change Theme
1. Click sun/moon icon in header
2. Theme persists across sessions

### Logout
1. Click "Logout" in header
2. Redirects to login page

---

**Need more help?** Check the full documentation files listed above!
