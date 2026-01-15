# Setup Checklist

Use this checklist to ensure your environment is properly configured.

## Prerequisites Installation

### macOS

- [ ] **Java 17+**
  ```bash
  # Check if installed
  java -version
  
  # Install using Homebrew
  brew install openjdk@17
  ```

- [ ] **Node.js 16+**
  ```bash
  # Check if installed
  node -v
  
  # Install using Homebrew
  brew install node
  ```

- [ ] **MySQL 8.0+**
  ```bash
  # Check if installed
  mysql --version
  
  # Install using Homebrew
  brew install mysql
  brew services start mysql
  ```

- [ ] **Maven** (Optional - project includes Maven wrapper)
  ```bash
  # Check if installed
  mvn -v
  
  # Install using Homebrew
  brew install maven
  ```

### Windows

- [ ] **Java 17+**
  ```cmd
  # Check if installed
  java -version
  
  # Download from: https://adoptium.net/
  # Or use: https://www.oracle.com/java/technologies/downloads/
  ```

- [ ] **Node.js 16+**
  ```cmd
  # Check if installed
  node -v
  
  # Download from: https://nodejs.org/
  ```

- [ ] **MySQL 8.0+**
  ```cmd
  # Check if installed
  mysql --version
  
  # Download from: https://dev.mysql.com/downloads/installer/
  ```

- [ ] **Maven** (Optional - project includes Maven wrapper)
  ```cmd
  # Check if installed
  mvn -v
  
  # Download from: https://maven.apache.org/download.cgi
  ```

## Configuration Steps

### 1. Database Configuration

- [ ] MySQL is running
  ```bash
  # macOS/Linux
  brew services list | grep mysql
  # or
  sudo systemctl status mysql
  
  # Windows
  net start | findstr MySQL
  ```

- [ ] Database credentials updated in `src/main/resources/application.properties`
  ```properties
  spring.datasource.username=root
  spring.datasource.password=your_password
  ```

- [ ] Test database connection
  ```bash
  mysql -u root -p
  ```

### 2. Email Configuration (Optional)

- [ ] Email settings configured in `src/main/resources/application.properties`
  ```properties
  spring.mail.username=your_email@gmail.com
  spring.mail.password=your_app_password
  ```

- [ ] For Gmail: App Password generated (not regular password)
  - Go to: https://myaccount.google.com/apppasswords
  - Generate app password for "Mail"

### 3. Project Setup

- [ ] Project downloaded/cloned
  ```bash
  git clone <repository-url>
  cd quotation-system
  ```

- [ ] Backend dependencies downloaded
  ```bash
  # macOS/Linux
  ./mvnw clean install
  
  # Windows
  mvnw.cmd clean install
  ```

- [ ] Frontend dependencies installed
  ```bash
  cd frontend
  npm install
  ```

## Running the Application

### Using Startup Scripts (Recommended)

**macOS/Linux:**
- [ ] Make scripts executable
  ```bash
  chmod +x start-backend.sh start-frontend.sh start-all.sh
  ```

- [ ] Run the application
  ```bash
  ./start-all.sh
  ```

**Windows:**
- [ ] Run the application
  ```cmd
  start-all.bat
  ```

### Manual Start

- [ ] Backend started successfully
  ```bash
  # macOS/Linux
  ./mvnw spring-boot:run
  
  # Windows
  mvnw.cmd spring-boot:run
  ```
  
  Wait for: `Started QuotationSystemApplication`

- [ ] Frontend started successfully
  ```bash
  cd frontend
  npm start
  ```
  
  Wait for: `Compiled successfully!`

## Verification

- [ ] Backend accessible at `http://localhost:8080`
  - Test: `curl http://localhost:8080/api/auth/login` (should return 400/405)

- [ ] Frontend accessible at `http://localhost:3000`
  - Opens in browser automatically

- [ ] Can login with default credentials
  - Username: `spadmin`
  - Password: `pass`

- [ ] Theme toggle works (sun/moon icon in header)

- [ ] Can navigate to different pages based on role

## Common Issues & Solutions

### Backend won't start

- [ ] Check Java version: `java -version` (should be 17+)
- [ ] Check MySQL is running
- [ ] Check port 8080 is not in use
- [ ] Check database credentials in `application.properties`
- [ ] Check logs for specific error messages

### Frontend won't start

- [ ] Check Node.js version: `node -v` (should be 16+)
- [ ] Check port 3000 is not in use
- [ ] Delete `node_modules` and `package-lock.json`, then run `npm install`
- [ ] Clear npm cache: `npm cache clean --force`

### Cannot login

- [ ] Backend is running and accessible
- [ ] Check browser console for errors (F12)
- [ ] Check Network tab for API calls
- [ ] Verify database has default user (check `schema.sql`)

### Theme not working

- [ ] Check browser localStorage is enabled
- [ ] Clear browser cache
- [ ] Check browser console for errors

## Next Steps

After successful setup:

1. [ ] Change default super admin password
2. [ ] Create additional admin and user accounts
3. [ ] Add companies and products
4. [ ] Create your first quotation
5. [ ] Configure email settings for notifications
6. [ ] Customize quotation templates

## Support

If you encounter issues not covered here:

1. Check the main README.md for detailed documentation
2. Review the troubleshooting section
3. Check application logs:
   - Backend: Console output or `backend.log`
   - Frontend: Browser console (F12) or `frontend.log`

## Environment Variables (Optional)

For production deployment, consider using environment variables:

```bash
# macOS/Linux
export DB_USERNAME=root
export DB_PASSWORD=your_password
export MAIL_USERNAME=your_email@gmail.com
export MAIL_PASSWORD=your_app_password

# Windows
set DB_USERNAME=root
set DB_PASSWORD=your_password
set MAIL_USERNAME=your_email@gmail.com
set MAIL_PASSWORD=your_app_password
```

Then update `application.properties`:
```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}
spring.mail.username=${MAIL_USERNAME:}
spring.mail.password=${MAIL_PASSWORD:}
```
