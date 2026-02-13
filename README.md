# Quotation Management System

A comprehensive web-based quotation management system built with Spring Boot and React. The system provides features for creating, managing, and tracking quotations with an approval workflow, follow-up tracking, PDF export, and email notifications.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [User Roles](#user-roles)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Features

### Core Features
- **Quotation Management**: Create, edit, view, and delete quotations
- **Item Management**: Add multiple products/items to quotations with quantities and prices
- **Company Management**: Manage client companies
- **Product Management**: Manage product catalog
- **User Management**: Manage system users with role-based access control

### Advanced Features
- **Approval Workflow**: Submit quotations for approval, approve or reject with reasons
- **Revision System**: Automatic creation of revisions when quotations are rejected
- **Follow-up Tracking**: Set and track follow-up dates for quotations
- **Dashboard**: View upcoming follow-ups with customizable date filters
- **PDF Export**: Export approved quotations as professional PDF documents
- **Email Notifications**: Automatic email notifications for approvals and rejections
- **Real-time Calculations**: Automatic calculation of item totals and quotation totals
- **Responsive Design**: Mobile-friendly interface with Tailwind CSS

### Technical Features
- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Three user roles (USER, ADMIN, SUPERADMIN)
- **Caching**: Performance optimization with Caffeine cache
- **Error Handling**: Comprehensive error handling with user-friendly messages
- **Form Validation**: Client-side and server-side validation
- **Property-Based Testing**: Comprehensive testing with jqwik and fast-check

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Security** with JWT
- **Spring Data JPA**
- **MySQL** database
- **iText7** for PDF generation
- **Spring Mail** for email notifications
- **Caffeine** for caching
- **jqwik** for property-based testing

### Frontend
- **React 18**
- **React Router** for navigation
- **Axios** for API calls
- **Tailwind CSS** for styling
- **React Icons** for icons
- **fast-check** for property-based testing

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **Node.js 16+** and **npm 8+**
- **MySQL 8.0+**
- **Git** (for cloning the repository)

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd quotation-system
```

### 2. Database Setup

Create a MySQL database for the application:

```sql
CREATE DATABASE quotation_db;
CREATE USER 'quotation_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON quotation_db.* TO 'quotation_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Backend Setup

Navigate to the project root directory and configure the database connection:

```bash
# Copy the example environment file
cp .env.example .env

# Edit .env with your database credentials
```

The `.env` file should contain:

```properties
DB_URL=jdbc:mysql://localhost:3306/quotation_db
DB_USERNAME=quotation_user
DB_PASSWORD=your_password
JWT_SECRET=your-secret-key-min-256-bits
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

Install backend dependencies:

```bash
./mvnw clean install
```

### 4. Frontend Setup

Navigate to the frontend directory and install dependencies:

```bash
cd frontend
npm install
```

Configure the frontend environment:

```bash
# Copy the example environment file
cp .env.example .env

# Edit .env with your backend URL
```

The `frontend/.env` file should contain:

```properties
REACT_APP_API_URL=http://localhost:8080/api
```

## Configuration

### Database Migration

The application uses Flyway for database migrations. Migrations are automatically applied on startup. Migration files are located in:

```
src/main/resources/db/migration/
```

### Email Configuration

For email notifications to work, configure your SMTP settings in the `.env` file. If using Gmail:

1. Enable 2-factor authentication on your Google account
2. Generate an App Password
3. Use the App Password in the `MAIL_PASSWORD` field

### JWT Configuration

Generate a secure JWT secret key (minimum 256 bits):

```bash
openssl rand -base64 32
```

Add the generated key to your `.env` file as `JWT_SECRET`.

## Running the Application

### Development Mode

#### Start the Backend

From the project root directory:

```bash
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

#### Start the Frontend

From the frontend directory:

```bash
cd frontend
npm start
```

The frontend will start on `http://localhost:3000`

### Production Mode

#### Build the Backend

```bash
./mvnw clean package -DskipTests
```

The JAR file will be created in `target/QuotationSystem-0.0.1-SNAPSHOT.jar`

#### Run the Backend JAR

```bash
java -jar target/QuotationSystem-0.0.1-SNAPSHOT.jar
```

#### Build the Frontend

```bash
cd frontend
npm run build
```

The production build will be created in `frontend/build/`

Serve the frontend using a web server like Nginx or Apache.

## Running Tests

### Backend Tests

Run all backend tests:

```bash
./mvnw test
```

Run specific test class:

```bash
./mvnw test -Dtest=QuotationServiceTest
```

Run property-based tests:

```bash
./mvnw test -Dtest=*PropertyTest
```

### Frontend Tests

Run all frontend tests:

```bash
cd frontend
npm test
```

Run tests with coverage:

```bash
npm test -- --coverage
```

## API Documentation

### Interactive Documentation

Once the application is running, access the interactive Swagger UI documentation at:

```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

The OpenAPI specification is available at:

```
http://localhost:8080/v3/api-docs
```

### Detailed Documentation

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for comprehensive API documentation including:
- All endpoints with request/response examples
- Authentication requirements
- Error codes and responses
- Validation rules

## Project Structure

```
quotation-system/
├── src/
│   ├── main/
│   │   ├── java/com/quotation/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── model/           # Domain models
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── db/migration/    # Database migrations
│   │       ├── templates/       # Email templates
│   │       └── application.properties
│   └── test/                    # Backend tests
├── frontend/
│   ├── public/                  # Static files
│   └── src/
│       ├── components/          # React components
│       │   ├── common/          # Reusable components
│       │   └── layout/          # Layout components
│       ├── contexts/            # React contexts
│       ├── hooks/               # Custom hooks
│       ├── pages/               # Page components
│       ├── services/            # API services
│       └── utils/               # Utility functions
├── .env                         # Backend environment variables
├── pom.xml                      # Maven configuration
└── README.md                    # This file
```

## User Roles

The system has three user roles with different permissions:

### USER
- View quotations
- Create and edit DRAFT quotations
- Export PDFs of approved quotations

### ADMIN
- All USER permissions
- Approve and reject quotations
- View dashboard with upcoming follow-ups
- Manage companies and products

### SUPERADMIN
- All ADMIN permissions
- Manage users (create, edit, delete)
- Full system access

### Default Users

After initial setup, you can create users through the SUPERADMIN interface. The first user should be created manually in the database or through a data migration script.

## Troubleshooting

### Backend Issues

**Problem:** Application fails to start with database connection error

**Solution:** 
- Verify MySQL is running: `sudo systemctl status mysql`
- Check database credentials in `.env`
- Ensure database exists: `mysql -u root -p -e "SHOW DATABASES;"`

**Problem:** JWT token errors

**Solution:**
- Ensure `JWT_SECRET` is set in `.env` and is at least 256 bits
- Clear browser localStorage and login again

**Problem:** Email notifications not working

**Solution:**
- Verify SMTP settings in `.env`
- Check if firewall is blocking SMTP port
- For Gmail, ensure App Password is used (not regular password)

### Frontend Issues

**Problem:** API calls failing with CORS errors

**Solution:**
- Verify backend is running on port 8080
- Check `REACT_APP_API_URL` in `frontend/.env`
- Ensure CORS is properly configured in backend

**Problem:** Build fails with dependency errors

**Solution:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Database Issues

**Problem:** Migration fails

**Solution:**
- Check Flyway migration files in `src/main/resources/db/migration/`
- Verify migration version numbers are sequential
- Check `flyway_schema_history` table for failed migrations
- If needed, repair: `./mvnw flyway:repair`

## Development Guidelines

### Code Style

- **Backend**: Follow Java naming conventions and Spring Boot best practices
- **Frontend**: Follow React best practices and use functional components with hooks
- **Formatting**: Use consistent indentation (2 spaces for JS/JSX, 4 spaces for Java)

### Git Workflow

1. Create a feature branch: `git checkout -b feature/your-feature-name`
2. Make your changes and commit: `git commit -m "Description of changes"`
3. Push to remote: `git push origin feature/your-feature-name`
4. Create a Pull Request

### Testing

- Write unit tests for all new features
- Write property-based tests for core business logic
- Ensure all tests pass before committing: `./mvnw test && cd frontend && npm test`
- Maintain test coverage above 80% for backend, 70% for frontend

## Performance Optimization

### Caching

The application uses Caffeine cache for frequently accessed data:
- Company list (5-minute TTL)
- Product list (5-minute TTL)

### Database Optimization

- Indexes are created on frequently queried columns
- JOIN queries are used to minimize database round trips
- Pagination is implemented for large result sets

## Security Considerations

- All passwords are hashed using BCrypt
- JWT tokens expire after a configurable period
- All API endpoints require authentication (except login)
- Role-based access control is enforced at controller and service layers
- Input validation is performed on both client and server sides
- SQL injection is prevented through parameterized queries

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch
3. Make your changes with appropriate tests
4. Ensure all tests pass
5. Submit a pull request with a clear description of changes

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Support

For support or questions:
- Email: support@quotationsystem.com
- Documentation: See [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- Issues: Create an issue in the repository

## Acknowledgments

- Spring Boot team for the excellent framework
- React team for the powerful UI library
- All contributors who have helped improve this project
