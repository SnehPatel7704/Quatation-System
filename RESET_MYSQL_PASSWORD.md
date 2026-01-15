# Reset MySQL Root Password - Manual Steps

## The Issue
MySQL root user has a password set, but we don't know what it is. The backend can't connect to the database.

## Solution: Manual Password Reset

### Step 1: Stop MySQL
```bash
brew services stop mysql
```

### Step 2: Start MySQL in Safe Mode (Skip Grant Tables)
Open a new terminal window and run:
```bash
sudo mysqld_safe --skip-grant-tables --skip-networking
```

**Important:** Keep this terminal window open! MySQL will run in the foreground.

### Step 3: Connect to MySQL (In a NEW Terminal Window)
Open a second terminal window and run:
```bash
mysql -u root
```

You should now be connected to MySQL without a password.

### Step 4: Reset the Password
In the MySQL prompt, run these commands:
```sql
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
EXIT;
```

### Step 5: Stop Safe Mode MySQL
Go back to the first terminal window where `mysqld_safe` is running and press `Ctrl+C` to stop it.

### Step 6: Start MySQL Normally
```bash
brew services start mysql
```

### Step 7: Test the Connection
```bash
mysql -u root -proot -e "SELECT 1 AS test"
```

You should see:
```
+------+
| test |
+------+
|    1 |
+------+
```

### Step 8: Update Application Properties
Make sure `src/main/resources/application.properties` has:
```properties
spring.datasource.password=root
```

### Step 9: Start the Backend
```bash
./start-backend.sh
```

## Alternative: Use a Different Password

If you want to use a different password (e.g., "mypassword"), in Step 4 use:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'mypassword';
```

Then update `application.properties`:
```properties
spring.datasource.password=mypassword
```

## Still Having Issues?

### Option A: Reinstall MySQL
```bash
brew services stop mysql
brew uninstall mysql
brew install mysql
brew services start mysql
```

After fresh install, MySQL usually has no password or password "root".

### Option B: Create a New User
If you can connect to MySQL with any account, create a new user:
```sql
CREATE USER 'quotation'@'localhost' IDENTIFIED BY 'quotation123';
GRANT ALL PRIVILEGES ON *.* TO 'quotation'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

Then update `application.properties`:
```properties
spring.datasource.username=quotation
spring.datasource.password=quotation123
```

## Need Help?

If you're still stuck, let me know:
1. What error message you're seeing
2. Whether you can connect to MySQL at all
3. If you remember setting a MySQL password during installation
