# Maven Wrapper Setup Issue - Solutions

## Problem
The Maven wrapper is trying to download but the JAR file isn't loading properly.

## Solution Options

### Option 1: Use Maven Directly (Easiest)

If you have Maven installed, you can use it directly instead of the wrapper:

**macOS/Linux:**
```bash
# Check if Maven is installed
mvn -v

# If installed, use these commands instead:
mvn spring-boot:run                    # Start backend
mvn clean install                      # Build project
mvn clean package                      # Create JAR
```

**Windows:**
```cmd
# Check if Maven is installed
mvn -v

# If installed, use these commands instead:
mvn spring-boot:run                    # Start backend
mvn clean install                      # Build project
mvn clean package                      # Create JAR
```

**Install Maven if needed:**

macOS:
```bash
brew install maven
```

Windows:
- Download from: https://maven.apache.org/download.cgi
- Or use: `choco install maven` (if you have Chocolatey)

### Option 2: Download Maven Wrapper JAR Manually

**macOS/Linux:**
```bash
# Create directory if it doesn't exist
mkdir -p .mvn/wrapper

# Download the wrapper JAR
curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

# Make mvnw executable
chmod +x mvnw

# Test it
./mvnw -v
```

**Windows (PowerShell):**
```powershell
# Create directory if it doesn't exist
New-Item -ItemType Directory -Force -Path .mvn\wrapper

# Download the wrapper JAR
Invoke-WebRequest -Uri "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" -OutFile ".mvn\wrapper\maven-wrapper.jar"

# Test it
.\mvnw.cmd -v
```

**Windows (Command Prompt with curl):**
```cmd
# Create directory
if not exist ".mvn\wrapper" mkdir .mvn\wrapper

# Download the wrapper JAR
curl -o .mvn\wrapper\maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

# Test it
mvnw.cmd -v
```

### Option 3: Use IDE (IntelliJ IDEA / Eclipse / VS Code)

Most IDEs have built-in Maven support:

**IntelliJ IDEA:**
1. Open the project
2. Right-click on `pom.xml`
3. Select "Add as Maven Project"
4. Use Maven panel to run goals

**Eclipse:**
1. Import as "Existing Maven Project"
2. Right-click project → Run As → Maven Build
3. Enter goal: `spring-boot:run`

**VS Code:**
1. Install "Maven for Java" extension
2. Use Maven sidebar to run goals

## Updated Startup Commands

Once you've chosen a solution, update your commands:

### If Using Maven Directly

**macOS/Linux:**
```bash
# Start backend
mvn spring-boot:run

# Or update start-backend.sh to use mvn instead of ./mvnw
```

**Windows:**
```cmd
# Start backend
mvn spring-boot:run

# Or update start-backend.bat to use mvn instead of mvnw.cmd
```

### If Maven Wrapper is Fixed

**macOS/Linux:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

## Quick Fix Scripts

### For macOS/Linux

Create a file `fix-maven.sh`:
```bash
#!/bin/bash
echo "Downloading Maven Wrapper JAR..."
mkdir -p .mvn/wrapper
curl -L -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
chmod +x mvnw
echo "Done! Try running: ./mvnw -v"
```

Run it:
```bash
chmod +x fix-maven.sh
./fix-maven.sh
```

### For Windows

Create a file `fix-maven.bat`:
```cmd
@echo off
echo Downloading Maven Wrapper JAR...
if not exist ".mvn\wrapper" mkdir .mvn\wrapper
curl -L -o .mvn\wrapper\maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
echo Done! Try running: mvnw.cmd -v
pause
```

Run it:
```cmd
fix-maven.bat
```

## Verify Installation

After applying any solution, verify it works:

**Maven Direct:**
```bash
mvn -v
```

**Maven Wrapper:**
```bash
# macOS/Linux
./mvnw -v

# Windows
mvnw.cmd -v
```

## Start the Backend

Once Maven is working, start the backend:

**macOS/Linux:**
```bash
# With Maven
mvn spring-boot:run

# With Maven Wrapper
./mvnw spring-boot:run
```

**Windows:**
```cmd
# With Maven
mvn spring-boot:run

# With Maven Wrapper
mvnw.cmd spring-boot:run
```

## Recommended Approach

**For Development:**
1. Install Maven directly (easier and more reliable)
2. Use `mvn` commands instead of wrapper
3. Update startup scripts to use `mvn` instead of `./mvnw` or `mvnw.cmd`

**Why Maven Direct is Better:**
- ✅ No wrapper download issues
- ✅ Faster startup
- ✅ Works consistently across projects
- ✅ Easier to troubleshoot

## Need Help?

If you're still having issues:

1. **Check Java version:**
   ```bash
   java -version
   # Should be 17 or higher
   ```

2. **Check if Maven is installed:**
   ```bash
   mvn -v
   ```

3. **Install Maven if needed:**
   - macOS: `brew install maven`
   - Windows: Download from https://maven.apache.org/download.cgi
   - Linux: `sudo apt install maven` or `sudo yum install maven`

4. **Use Maven directly** - it's simpler and more reliable for development
