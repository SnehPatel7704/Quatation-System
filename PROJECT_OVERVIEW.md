# Project Overview

## 📋 Table of Contents
- [Introduction](#introduction)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Key Features](#key-features)
- [User Roles & Permissions](#user-roles--permissions)
- [Getting Started](#getting-started)
- [Documentation](#documentation)

## Introduction

The Quotation Management System is a full-stack web application designed to streamline the process of creating, managing, and sending quotations to clients. It features role-based access control, email notifications, and a modern, responsive user interface with dark/light theme support.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     Client Browser                       │
│                  (React + Tailwind CSS)                  │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP/REST API
                     │ (JWT Authentication)
┌────────────────────▼────────────────────────────────────┐
│                  Spring Boot Backend                     │
│  ┌──────────────────────────────────────────────────┐  │
│  │  Controllers (REST Endpoints)                     │  │
│  └────────────┬─────────────────────────────────────┘  │
│  ┌────────────▼─────────────────────────────────────┐  │
│  │  Services (Business Logic)                        │  │
│  └────────────┬─────────────────────────────────────┘  │
│  ┌────────────▼─────────────────────────────────────┐  │
│  │  Repositories (Data Access - JDBC)                │  │
│  └────────────┬─────────────────────────────────────┘  │
└───────────────┼──────────────────────────────────────────┘
                │
┌───────────────▼──────────────────────────────────────────┐
│                    MySQL Database                         │
│  (Users, Quotations, Companies, Products, Templates)     │
└───────────────────────────────────────────────────────────┘
```

## Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming language |
| Spring Boot | 3.2.1 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| JWT | 0.11.5 | Token-based authentication |
| Spring JDBC | 3.2.1 | Database access |
| MySQL | 8.0+ | Database |
| JavaMail | Latest | Email notifications |
| Apache POI | 5.2.5 | Excel file processing |
| Lombok | Latest | Reduce boilerplate code |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18.2.0 | UI framework |
| React Router | 6.20.0 | Client-side routing |
| Tailwind CSS | 3.3.6 | Styling framework |
| Axios | 1.6.2 | HTTP client |
| React Icons | 4.12.0 | Icon library |
| Context API | Built-in | State management |

### Development Tools
| Tool | Purpose |
|------|---------|
| Maven | Backend build tool |
| npm | Frontend package manager |
| Git | Version control |

## Project Structure

```
quotation-system/
│
├── 📁 Backend (Spring Boot)
│   ├── src/main/
│   │   ├── java/com/quotation/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   │
│   │   │   ├── controller/          # REST API endpoints
│   │   │   │   ├── AuthRestController.java
│   │   │   │   ├── QuotationController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── CompanyController.java
│   │   │   │   └── ProductController.java
│   │   │   │
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── UserRequest.java
│   │   │   │   └── QuotationRequest.java
│   │   │   │
│   │   │   ├── model/               # Entity models
│   │   │   │   ├── User.java
│   │   │   │   ├── Quotation.java
│   │   │   │   ├── QuotationItem.java
│   │   │   │   ├── Company.java
│   │   │   │   ├── Product.java
│   │   │   │   └── QuotationTemplate.java
│   │   │   │
│   │   │   ├── repository/          # Data access layer
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── QuotationRepository.java
│   │   │   │   ├── CompanyRepository.java
│   │   │   │   └── ProductRepository.java
│   │   │   │
│   │   │   ├── service/             # Business logic
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── QuotationService.java
│   │   │   │   └── EmailService.java
│   │   │   │
│   │   │   └── QuotationSystemApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   │
│   └── pom.xml
│
├── 📁 Frontend (React)
│   ├── public/
│   │   └── index.html
│   │
│   ├── src/
│   │   ├── components/
│   │   │   ├── common/
│   │   │   │   └── PrivateRoute.js
│   │   │   └── layout/
│   │   │       ├── Header.js
│   │   │       └── Layout.js
│   │   │
│   │   ├── contexts/
│   │   │   ├── AuthContext.js       # Authentication state
│   │   │   └── ThemeContext.js      # Theme (dark/light) state
│   │   │
│   │   ├── pages/
│   │   │   ├── auth/
│   │   │   │   └── Login.js
│   │   │   ├── dashboard/
│   │   │   │   └── Dashboard.js
│   │   │   ├── quotations/
│   │   │   │   ├── QuotationList.js
│   │   │   │   ├── QuotationCreate.js
│   │   │   │   └── QuotationEdit.js
│   │   │   ├── users/
│   │   │   │   └── UserManagement.js
│   │   │   ├── companies/
│   │   │   │   └── CompanyManagement.js
│   │   │   └── products/
│   │   │       └── ProductManagement.js
│   │   │
│   │   ├── services/
│   │   │   ├── api.js               # Axios configuration
│   │   │   ├── authService.js
│   │   │   ├── quotationService.js
│   │   │   ├── userService.js
│   │   │   ├── companyService.js
│   │   │   └── productService.js
│   │   │
│   │   ├── App.js
│   │   ├── index.js
│   │   └── index.css
│   │
│   ├── package.json
│   ├── tailwind.config.js
│   └── postcss.config.js
│
├── 📁 Scripts
│   ├── setup.sh / setup.bat         # Initial setup
│   ├── start-all.sh / start-all.bat # Start both servers
│   ├── start-backend.sh / .bat      # Start backend only
│   └── start-frontend.sh / .bat     # Start frontend only
│
├── 📁 Documentation
│   ├── README.md                    # Main documentation
│   ├── SETUP_CHECKLIST.md          # Setup guide
│   ├── SCRIPTS_GUIDE.md            # Scripts documentation
│   └── PROJECT_OVERVIEW.md         # This file
│
└── .gitignore
```

## Key Features

### 1. Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- Secure password encryption (BCrypt)
- Token expiration and refresh

### 2. User Management (Super Admin Only)
- Create, read, update, delete users
- Assign roles (SUPERADMIN, ADMIN, USER)
- Enable/disable user accounts

### 3. Quotation Management
- Create quotations with multiple items
- Edit and update quotations
- Approve quotations (Admin+)
- Send quotations via email
- Track quotation status
- Auto-generate quotation numbers

### 4. Company Management (Admin+)
- Maintain company database
- Store contact information
- Link companies to quotations

### 5. Product Management (Admin+)
- Product catalog
- Base pricing
- Product descriptions

### 6. Email Notifications
- Send quotations to admins for approval
- Send approved quotations to clients
- Configurable SMTP settings

### 7. UI/UX Features
- Dark/Light theme toggle
- Responsive design (mobile-friendly)
- Modern, clean interface
- Loading states and error handling
- Intuitive navigation

## User Roles & Permissions

### Super Admin
**Full system access:**
- ✅ All Admin permissions
- ✅ Create/edit/delete users
- ✅ Assign roles
- ✅ System configuration

### Admin
**Management access:**
- ✅ Create/edit/delete quotations
- ✅ Approve quotations
- ✅ Send quotations to clients
- ✅ Manage companies
- ✅ Manage products
- ❌ User management

### User
**View-only access:**
- ✅ View quotations
- ✅ View companies
- ✅ View products
- ❌ Create/edit/delete
- ❌ Approve quotations
- ❌ User management

## Getting Started

### Quick Start (3 Steps)

1. **Run Setup**
   ```bash
   # macOS/Linux
   chmod +x setup.sh && ./setup.sh
   
   # Windows
   setup.bat
   ```

2. **Configure Database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Start Application**
   ```bash
   # macOS/Linux
   ./start-all.sh
   
   # Windows
   start-all.bat
   ```

4. **Access Application**
   
   Open: http://localhost:3000
   
   Login: `spadmin` / `pass`

### Detailed Setup

See [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) for comprehensive setup instructions.

## Documentation

| Document | Description |
|----------|-------------|
| [README.md](README.md) | Main project documentation |
| [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) | Step-by-step setup guide |
| [SCRIPTS_GUIDE.md](SCRIPTS_GUIDE.md) | Detailed scripts documentation |
| [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md) | This document |

## API Endpoints

### Authentication
```
POST   /api/auth/login          # User login
GET    /api/auth/me             # Get current user
```

### Quotations
```
GET    /api/quotations           # List all quotations
GET    /api/quotations/{id}      # Get quotation by ID
POST   /api/quotations           # Create quotation
PUT    /api/quotations/{id}      # Update quotation
DELETE /api/quotations/{id}      # Delete quotation
POST   /api/quotations/{id}/approve  # Approve quotation
POST   /api/quotations/{id}/send     # Send to client
```

### Users (Super Admin)
```
GET    /api/superadmin/users     # List users
POST   /api/superadmin/users     # Create user
PUT    /api/superadmin/users/{id}  # Update user
DELETE /api/superadmin/users/{id}  # Delete user
```

### Companies
```
GET    /api/companies            # List companies
GET    /api/companies/{id}       # Get company
POST   /api/companies            # Create company
PUT    /api/companies/{id}       # Update company
DELETE /api/companies/{id}       # Delete company
```

### Products
```
GET    /api/products             # List products
GET    /api/products/{id}        # Get product
POST   /api/products             # Create product
PUT    /api/products/{id}        # Update product
DELETE /api/products/{id}        # Delete product
```

## Database Schema

### Tables
- `users` - System users with roles
- `companies` - Client companies
- `products` - Product catalog
- `quotations` - Quotation headers
- `quotation_items` - Quotation line items
- `quotation_templates` - Custom templates

### Relationships
```
users (1) ──── (N) quotations
companies (1) ──── (N) quotations
quotations (1) ──── (N) quotation_items
products (1) ──── (N) quotation_items
users (1) ──── (N) quotation_templates
```

## Development Workflow

### Adding a New Feature

1. **Backend:**
   - Create model in `model/`
   - Create repository in `repository/`
   - Create service in `service/`
   - Create controller in `controller/`
   - Add security rules in `SecurityConfig.java`

2. **Frontend:**
   - Create service in `services/`
   - Create page component in `pages/`
   - Add route in `App.js`
   - Add navigation link in `Header.js`

3. **Testing:**
   - Test API endpoints
   - Test UI components
   - Test role-based access

## Deployment

### Production Build

**Backend:**
```bash
./mvnw clean package -DskipTests
java -jar target/quotation-system-1.0.0.jar
```

**Frontend:**
```bash
cd frontend
npm run build
# Deploy 'build' folder to web server
```

### Environment Variables

For production, use environment variables:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

## Future Enhancements

- [ ] PDF generation for quotations
- [ ] Excel import/export
- [ ] Custom quotation templates
- [ ] File upload for quotations
- [ ] Advanced reporting and analytics
- [ ] Multi-language support
- [ ] Audit logging
- [ ] Two-factor authentication
- [ ] API rate limiting
- [ ] Quotation versioning

## Support & Contribution

### Getting Help
1. Check documentation files
2. Review troubleshooting sections
3. Check application logs
4. Review error messages

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

MIT License - See LICENSE file for details

---

**Last Updated:** January 2024
**Version:** 1.0.0
