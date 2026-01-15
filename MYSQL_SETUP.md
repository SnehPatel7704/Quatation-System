# MySQL Setup Guide

## Current Status

✅ Frontend is running on http://localhost:3000
⏳ Backend needs MySQL to be configured

## Quick Setup

### Option 1: Reset MySQL Root Password (Recommended)

**Step 1: Stop MySQL**
```bash
brew services stop mysql
```

**Step 2: Start MySQL in safe mode**
```bash
mysqld_safe --skip-grant-tables &
```

**Step 3: Connect and reset password**
```bash
mysql -u root

# In MySQL prompt:
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
EXIT;
```

**Step 4: Stop safe mode and restart MySQL**
```bash
# Find and kill mysqld_safe process
ps aux | grep mysqld
kill <PID>

# Restart MySQL normally
brew services start mysql
```

**Step 5: Test connection**
```bash
mysql -u root -proot -e "SELECT 1"
```

### Option 2: Use Empty Password

**Update `src/main/resources/application.properties`:**
```properties
spring.datasource.password=
```

Then try connecting:
```bash
mysql -u root
```

### Option 3: Create New MySQL User

```bash
# Connect as root (if you know the password)
mysql -u root -p

# Create new user
CREATE USER 'quotation'@'localhost' IDENTIFIED BY 'quotation123';
GRANT ALL PRIVILEGES ON quotation_db.* TO 'quotation'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

**Update `src/main/resources/application.properties`:**
```properties
spring.datasource.username=quotation
spring.datasource.password=quotation123
```

## After MySQL is Configured

### Start the Backend

```bash
./start-backend.sh
```

The backend will:
1. Connect to MySQL
2. Create the `quotation_db` database automatically
3. Create all tables from `schema.sql`
4. Insert default users (superadmin, admin, user)
5. Start on http://localhost:8080

### Access the Application

1. **Open browser:** http://localhost:3000
2. **Login with:**
   - Username: `spadmin`
   - Password: `pass`

## Troubleshooting

### Can't connect to MySQL

**Check if MySQL is running:**
```bash
brew services list | grep mysql
```

**Start MySQL:**
```bash
brew services start mysql
```

### "Access denied" error

**Option A: Reset password (see Option 1 above)**

**Option B: Check what password MySQL expects:**
```bash
# Try common passwords
mysql -u root -proot
mysql -u root -p
mysql -u root --password=""
```

### MySQL not installed

```bash
brew install mysql
brew services start mysql
```

## Current Configuration

**File:** `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quotation_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

**Change the password to match your MySQL setup!**

## Quick Test

Once MySQL is configured, test the connection:

```bash
mysql -u root -pYOUR_PASSWORD -e "SHOW DATABASES;"
```

Then start the backend:

```bash
./start-backend.sh
```

## Need Help?

1. Check MySQL is running: `brew services list`
2. Check MySQL logs: `tail -f /opt/homebrew/var/mysql/*.err`
3. See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for more help
