# 📑 Project Index

Complete list of all files and documentation in the Quotation Management System.

## 🚀 Start Here

| File | Description | When to Use |
|------|-------------|-------------|
| **[GET_STARTED.md](GET_STARTED.md)** | 5-minute quick start guide | First time setup |
| **[CREDENTIALS.md](CREDENTIALS.md)** | Default login credentials | Login information |
| **[QUICK_REFERENCE.md](QUICK_REFERENCE.md)** | Quick commands cheat sheet | Daily reference |

## 📚 Documentation

| File | Description | Audience |
|------|-------------|----------|
| **[README.md](README.md)** | Main project documentation | Everyone |
| **[CREDENTIALS.md](CREDENTIALS.md)** | Login credentials & security | Everyone |
| **[PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md)** | Architecture & technical details | Developers |
| **[SETUP_CHECKLIST.md](SETUP_CHECKLIST.md)** | Step-by-step setup guide | New users |
| **[SCRIPTS_GUIDE.md](SCRIPTS_GUIDE.md)** | Detailed scripts documentation | All users |
| **[INDEX.md](INDEX.md)** | This file - complete file listing | Navigation |

## 🔧 Scripts (macOS/Linux)

| File | Purpose |
|------|---------|
| `setup.sh` | Initial project setup |
| `start-all.sh` | Start both backend and frontend |
| `start-backend.sh` | Start backend only |
| `start-frontend.sh` | Start frontend only |

## 🔧 Scripts (Windows)

| File | Purpose |
|------|---------|
| `setup.bat` | Initial project setup |
| `start-all.bat` | Start both backend and frontend |
| `start-backend.bat` | Start backend only |
| `start-frontend.bat` | Start frontend only |

## 📦 Configuration Files

### Root Level
| File | Purpose |
|------|---------|
| `pom.xml` | Maven configuration (backend dependencies) |
| `.gitignore` | Git ignore rules |

### Backend Configuration
| File | Location | Purpose |
|------|----------|---------|
| `application.properties` | `src/main/resources/` | Database, email, server config |
| `schema.sql` | `src/main/resources/` | Database schema and initial data |

### Frontend Configuration
| File | Location | Purpose |
|------|----------|---------|
| `package.json` | `frontend/` | npm dependencies and scripts |
| `tailwind.config.js` | `frontend/` | Tailwind CSS configuration |
| `postcss.config.js` | `frontend/` | PostCSS configuration |

## 🗂️ Source Code Structure

### Backend (Java/Spring Boot)

```
src/main/java/com/quotation/
├── config/                      # Configuration classes
│   ├── SecurityConfig.java      # Spring Security + JWT
│   ├── JwtUtil.java            # JWT token utilities
│   └── JwtAuthenticationFilter.java
│
├── controller/                  # REST API endpoints
│   ├── AuthRestController.java  # Authentication
│   ├── QuotationController.java # Quotations CRUD
│   ├── UserController.java      # User management
│   ├── CompanyController.java   # Company management
│   └── ProductController.java   # Product management
│
├── dto/                        # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── UserRequest.java
│   └── QuotationRequest.java
│
├── model/                      # Entity models
│   ├── User.java
│   ├── Quotation.java
│   ├── QuotationItem.java
│   ├── Company.java
│   ├── Product.java
│   └── QuotationTemplate.java
│
├── repository/                 # Data access (JDBC)
│   ├── UserRepository.java
│   ├── QuotationRepository.java
│   ├── CompanyRepository.java
│   └── ProductRepository.java
│
├── service/                    # Business logic
│   ├── CustomUserDetailsService.java
│   ├── QuotationService.java
│   └── EmailService.java
│
└── QuotationSystemApplication.java  # Main application
```

### Frontend (React)

```
frontend/src/
├── components/
│   ├── common/
│   │   └── PrivateRoute.js     # Protected route wrapper
│   └── layout/
│       ├── Header.js           # Navigation header
│       └── Layout.js           # Page layout wrapper
│
├── contexts/
│   ├── AuthContext.js          # Authentication state
│   └── ThemeContext.js         # Theme (dark/light) state
│
├── pages/
│   ├── auth/
│   │   └── Login.js            # Login page
│   ├── dashboard/
│   │   └── Dashboard.js        # Main dashboard
│   ├── quotations/
│   │   ├── QuotationList.js    # List all quotations
│   │   ├── QuotationCreate.js  # Create new quotation
│   │   └── QuotationEdit.js    # Edit quotation
│   ├── users/
│   │   └── UserManagement.js   # User CRUD (Super Admin)
│   ├── companies/
│   │   └── CompanyManagement.js # Company CRUD
│   └── products/
│       └── ProductManagement.js # Product CRUD
│
├── services/
│   ├── api.js                  # Axios configuration
│   ├── authService.js          # Authentication API
│   ├── quotationService.js     # Quotation API
│   ├── userService.js          # User API
│   ├── companyService.js       # Company API
│   └── productService.js       # Product API
│
├── App.js                      # Main app component
├── index.js                    # React entry point
└── index.css                   # Global styles (Tailwind)
```

## 🎨 Frontend Assets

| File | Location | Purpose |
|------|----------|---------|
| `index.html` | `frontend/public/` | HTML template |
| `index.css` | `frontend/src/` | Global CSS with Tailwind |
| `App.js` | `frontend/src/` | Main React component |

## 📊 Database

| File | Location | Purpose |
|------|----------|---------|
| `schema.sql` | `src/main/resources/` | Database schema, tables, default data |

### Tables Created
- `users` - System users with roles
- `companies` - Client companies
- `products` - Product catalog
- `quotations` - Quotation headers
- `quotation_items` - Quotation line items
- `quotation_templates` - Custom templates

## 🔐 Security Files

| Component | Location | Purpose |
|-----------|----------|---------|
| `SecurityConfig.java` | `src/main/java/com/quotation/config/` | Spring Security configuration |
| `JwtUtil.java` | `src/main/java/com/quotation/config/` | JWT token generation/validation |
| `JwtAuthenticationFilter.java` | `src/main/java/com/quotation/config/` | JWT request filter |

## 📝 Documentation Files Summary

| File | Lines | Purpose |
|------|-------|---------|
| GET_STARTED.md | ~300 | Quick start guide |
| README.md | ~400 | Main documentation |
| PROJECT_OVERVIEW.md | ~600 | Architecture details |
| SETUP_CHECKLIST.md | ~400 | Setup checklist |
| SCRIPTS_GUIDE.md | ~500 | Scripts documentation |
| QUICK_REFERENCE.md | ~400 | Quick reference |
| INDEX.md | ~200 | This file |

## 🚀 Quick Navigation

### I want to...

| Task | Go to |
|------|-------|
| Get started quickly | [GET_STARTED.md](GET_STARTED.md) |
| See quick commands | [QUICK_REFERENCE.md](QUICK_REFERENCE.md) |
| Understand architecture | [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md) |
| Setup step-by-step | [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) |
| Learn about scripts | [SCRIPTS_GUIDE.md](SCRIPTS_GUIDE.md) |
| See all features | [README.md](README.md) |
| Find a specific file | This file (INDEX.md) |

## 📦 Dependencies

### Backend Dependencies (pom.xml)
- Spring Boot Starter Web
- Spring Boot Starter JDBC
- Spring Boot Starter Security
- Spring Boot Starter Mail
- MySQL Connector
- JWT (jjwt)
- Apache POI
- Lombok
- Validation API

### Frontend Dependencies (package.json)
- react
- react-dom
- react-router-dom
- axios
- react-icons
- tailwindcss
- autoprefixer
- postcss

## 🔄 Build Artifacts

### Backend
- `target/` - Maven build output
- `target/quotation-system-1.0.0.jar` - Executable JAR

### Frontend
- `frontend/node_modules/` - npm packages
- `frontend/build/` - Production build

## 📋 Log Files (Generated at Runtime)

| File | Created By | Contains |
|------|------------|----------|
| `backend.log` | start-all.sh | Backend server logs |
| `frontend.log` | start-all.sh | Frontend server logs |

## 🎯 Entry Points

| Type | File | Purpose |
|------|------|---------|
| Backend | `QuotationSystemApplication.java` | Spring Boot main class |
| Frontend | `index.js` | React entry point |
| Database | `schema.sql` | Database initialization |

## 🌐 URLs & Ports

| Service | URL | Port |
|---------|-----|------|
| Frontend | http://localhost:3000 | 3000 |
| Backend API | http://localhost:8080 | 8080 |
| MySQL | localhost | 3306 |

## 📞 Support Resources

| Resource | Location |
|----------|----------|
| Troubleshooting | README.md, SETUP_CHECKLIST.md |
| API Documentation | PROJECT_OVERVIEW.md |
| Script Help | SCRIPTS_GUIDE.md |
| Quick Help | QUICK_REFERENCE.md |

---

## 🗺️ File Tree

```
quotation-system/
├── 📄 Documentation
│   ├── GET_STARTED.md
│   ├── README.md
│   ├── PROJECT_OVERVIEW.md
│   ├── SETUP_CHECKLIST.md
│   ├── SCRIPTS_GUIDE.md
│   ├── QUICK_REFERENCE.md
│   └── INDEX.md (this file)
│
├── 🔧 Scripts (macOS/Linux)
│   ├── setup.sh
│   ├── start-all.sh
│   ├── start-backend.sh
│   └── start-frontend.sh
│
├── 🔧 Scripts (Windows)
│   ├── setup.bat
│   ├── start-all.bat
│   ├── start-backend.bat
│   └── start-frontend.bat
│
├── ⚙️ Configuration
│   ├── pom.xml
│   └── .gitignore
│
├── 📁 Backend (src/main/)
│   ├── java/com/quotation/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── QuotationSystemApplication.java
│   └── resources/
│       ├── application.properties
│       └── schema.sql
│
└── 📁 Frontend (frontend/)
    ├── public/
    │   └── index.html
    ├── src/
    │   ├── components/
    │   ├── contexts/
    │   ├── pages/
    │   ├── services/
    │   ├── App.js
    │   ├── index.js
    │   └── index.css
    ├── package.json
    ├── tailwind.config.js
    └── postcss.config.js
```

---

**Total Files:** ~80+ source files + documentation
**Total Lines of Code:** ~5,000+ lines
**Documentation:** ~2,500+ lines

**Last Updated:** January 2024
