# 🔑 Default Login Credentials

## Default Users

The system comes with three pre-configured users for testing and initial setup.

### Super Admin Account
```
Username: spadmin
Password: password
Email:    spadmin@example.com
Role:     SUPERADMIN
```

**Permissions:**
- ✅ Full system access
- ✅ User management (create, edit, delete users)
- ✅ Assign roles to users
- ✅ All Admin permissions
- ✅ All User permissions

---

### Admin Account
```
Username: admin
Password: password
Email:    admin@example.com
Role:     ADMIN
```

**Permissions:**
- ✅ Create, edit, delete quotations
- ✅ Approve quotations
- ✅ Send quotations to clients
- ✅ Manage companies (create, edit, delete)
- ✅ Manage products (create, edit, delete)
- ✅ View all quotations
- ❌ User management (cannot create/edit users)

---

### Regular User Account
```
Username: user
Password: password
Email:    user@example.com
Role:     USER
```

**Permissions:**
- ✅ View quotations
- ✅ View companies
- ✅ View products
- ❌ Create, edit, or delete anything
- ❌ Approve quotations
- ❌ User management

---

## 🔐 Security Notes

### ⚠️ IMPORTANT - Change Default Passwords!

**For production use, you MUST change these default passwords immediately!**

### How to Change Password

1. **Login as Super Admin**
   - Go to http://localhost:3000
   - Login with `spadmin` / `password`

2. **Navigate to Users**
   - Click "Users" in the navigation menu

3. **Edit User**
   - Click the edit icon next to the user
   - Enter a new strong password
   - Save changes

### Password Requirements

For security, use passwords that:
- Are at least 8 characters long
- Include uppercase and lowercase letters
- Include numbers
- Include special characters
- Are unique (not used elsewhere)

---

## 🧪 Testing Different Roles

### Test Super Admin Features
```
Login: spadmin / password
Test: User management, full access
```

### Test Admin Features
```
Login: admin / password
Test: Quotation creation, company/product management
```

### Test User Features (View Only)
```
Login: user / password
Test: View-only access to quotations
```

---

## 🔄 Password Reset

If you forget a password, you can reset it directly in the database:

### Generate New Password Hash

**Using Online BCrypt Generator:**
1. Go to: https://bcrypt-generator.com/
2. Enter your new password
3. Use rounds: 10
4. Copy the generated hash

**Or using command line:**
```bash
# Using htpasswd (if installed)
htpasswd -bnBC 10 "" your_password | tr -d ':\n'
```

### Update Database

```sql
-- Connect to MySQL
mysql -u root -p quotation_db

-- Update password (replace with your BCrypt hash)
UPDATE users 
SET password = '$2a$10$YOUR_NEW_BCRYPT_HASH_HERE' 
WHERE username = 'spadmin';
```

---

## 📝 Creating New Users

### Via Super Admin Panel (Recommended)

1. Login as `spadmin`
2. Go to "Users" menu
3. Click "Add User"
4. Fill in the form:
   - Username
   - Password
   - Email
   - Role (SUPERADMIN, ADMIN, or USER)
5. Click "Save"

### Via Database (Advanced)

```sql
-- Insert new user
INSERT INTO users (username, password, email, role, enabled) 
VALUES (
    'newuser',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'newuser@example.com',
    'ADMIN',
    true
);
```

---

## 🎯 Quick Login Reference

| Purpose | Username | Password | Role |
|---------|----------|----------|------|
| Full Access | `spadmin` | `password` | SUPERADMIN |
| Management | `admin` | `password` | ADMIN |
| View Only | `user` | `password` | USER |

---

## 🔒 JWT Token Information

- **Token Type:** Bearer
- **Expiration:** 24 hours
- **Storage:** Browser localStorage
- **Header:** `Authorization: Bearer <token>`

### Token Expiry

When your token expires (after 24 hours):
1. You'll be automatically logged out
2. Simply login again to get a new token
3. No data is lost

---

## 🛡️ Security Best Practices

### For Development
- ✅ Use default credentials for testing
- ✅ Test all three roles
- ✅ Keep credentials in this file for reference

### For Production
- ❌ Never use default passwords
- ✅ Change all passwords immediately
- ✅ Use strong, unique passwords
- ✅ Enable HTTPS
- ✅ Use environment variables for sensitive data
- ✅ Implement password complexity rules
- ✅ Enable two-factor authentication (future feature)
- ✅ Regular security audits
- ✅ Monitor login attempts

---

## 📧 Email Configuration

For email notifications to work, configure SMTP in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**For Gmail:**
1. Enable 2-Step Verification
2. Generate App Password: https://myaccount.google.com/apppasswords
3. Use the app password (not your regular password)

---

## 🔍 Troubleshooting Login Issues

### Cannot Login

**Check:**
1. ✅ Backend is running (http://localhost:8080)
2. ✅ Database is running and accessible
3. ✅ Username and password are correct
4. ✅ User account is enabled
5. ✅ Browser console for errors (F12)

### "Invalid Credentials" Error

**Solutions:**
1. Double-check username and password
2. Ensure caps lock is off
3. Try copying credentials from this file
4. Check database has default users:
   ```sql
   SELECT username, email, role, enabled FROM users;
   ```

### Token Expired

**Solution:**
- Simply login again
- Token expires after 24 hours

### Account Disabled

**Solution:**
- Login as Super Admin
- Go to Users
- Enable the account

---

## 📱 Access URLs

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Login Page | http://localhost:3000/login |

---

## 🎓 Learning Path

### New to the System?

1. **Start with User role** (`user` / `password`)
   - Explore the interface
   - View quotations, companies, products
   - Understand the view-only limitations

2. **Try Admin role** (`admin` / `password`)
   - Create a quotation
   - Add companies and products
   - Approve and send quotations

3. **Use Super Admin** (`spadmin` / `password`)
   - Create new users
   - Assign different roles
   - Manage the entire system

---

## 💡 Pro Tips

1. **Keep this file handy** during development
2. **Test with all three roles** to understand permissions
3. **Create custom users** for your team members
4. **Document any new users** you create
5. **Change passwords** before deploying to production

---

**Remember:** These are DEFAULT credentials for DEVELOPMENT ONLY!

**For production, always use strong, unique passwords!**
