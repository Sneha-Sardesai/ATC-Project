# How to Apply Runway Assignment Fixes - Complete Guide

Since MySQL is not in your system PATH, here are the best ways to apply the database fixes:

---

## ✅ **RECOMMENDED: Method 1 - MySQL Workbench (Easiest)**

MySQL Workbench is the easiest and most visual way to apply the fixes.

### Steps:

1. **Open MySQL Workbench**
   - Look for "MySQL Workbench" in your Start Menu or Applications
   - Double-click to open

2. **Connect to your database**
   - You should see a connection named "Local instance MySQL80" or similar
   - Click on it to connect
   - Enter your MySQL password if prompted (usually "root")

3. **Select the ATC_DB database**
   - On the left panel, you should see "ATC_DB"
   - Click on it to make it the active database

4. **Open the SQL file**
   - Click menu: **File** → **Open SQL Script**
   - Navigate to: `dream project/database/apply_fixes.sql`
   - Click "Open"

5. **Execute the script**
   - The SQL script appears in the editor
   - Click the **Lightning Icon** (Execute) button at the top
   - Or press **Ctrl+Enter**

6. **Verify it worked**
   - You should see "Database fixes applied successfully!" at the bottom
   - Check if there were any errors in the Output tab

7. **Restart your backend server**
   ```bash
   cd backend
   java -cp bin:lib/* api.SimpleServer
   ```

---

## Method 2 - PowerShell Script

If you prefer command-line tools:

### Steps:

1. **Open PowerShell**
   - Right-click on the desktop
   - Select "Open PowerShell window here"
   - Or search "PowerShell" in Start Menu

2. **Navigate to database directory**

   ```powershell
   cd "database"
   ```

3. **Allow script execution** (one-time only)

   ```powershell
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
   ```

4. **Run the PowerShell script**

   ```powershell
   .\apply_fixes.ps1
   ```

5. **Enter credentials when prompted**
   - Username: `root` (or your MySQL username)
   - Password: (enter your MySQL password)

6. **Restart your backend server**
   ```bash
   cd backend
   java -cp bin:lib/* api.SimpleServer
   ```

---

## Method 3 - Command Prompt / Batch File

If you have MySQL installed but not in PATH, update the batch file first:

### Steps:

1. **Find your MySQL installation**
   - Look for MySQL in `C:\Program Files`
   - You should find something like `C:\Program Files\MySQL\MySQL Server 8.0\bin`

2. **Edit apply_fixes.bat**
   - Right-click `apply_fixes.bat`
   - Select "Edit" (or open with Notepad)
   - Find this line: `set MYSQL_PATH=`
   - Replace with your MySQL path:
     ```batch
     set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe
     ```
   - Save the file

3. **Run the updated batch file**
   - Double-click `apply_fixes.bat`
   - Enter your MySQL password when prompted

4. **Restart your backend server**
   ```bash
   cd backend
   java -cp bin:lib/* api.SimpleServer
   ```

---

## Method 4 - Manual SQL Execution

If all else fails, manually run the SQL commands:

### Steps:

1. **Open MySQL Workbench**

2. **Create a new SQL tab**
   - Click **File** → **New Query Tab**
   - Or press **Ctrl+T**

3. **Copy the SQL commands**
   - Open `apply_fixes.sql` in a text editor
   - Copy all the content

4. **Paste and execute**
   - Right-click in the SQL editor
   - Select "Paste"
   - Click the Lightning icon to execute
   - Or press **Ctrl+Enter**

5. **Restart your backend server**
   ```bash
   cd backend
   java -cp bin:lib/* api.SimpleServer
   ```

---

## Verify the Fix Works

After applying the database fixes:

1. **Restart the backend server**
2. **Run the frontend**
3. **Login and select a flight**
4. **Click "View Details"**
5. **Click "Assign Runway"**
6. **Enter a runway ID (e.g., 1, 2, 3)**
7. **You should see success message ✅**
8. **Click "Refresh" to verify data persists**

---

## What Gets Fixed?

The script applies these changes:

### 1. **Trigger Fix**

- Allows runway updates without gate assignment validation
- Only validates gates when gate_id is actually being changed

### 2. **Procedure Updates**

- AssignRunway: `UPDATE flights SET runway_id = ? WHERE flight_id = ?`
- AssignGate: `UPDATE flights SET gate_id = ?, status = 'GATE_ASSIGNED' WHERE flight_id = ?`

### 3. **Result**

- ✅ Runway assignments work without errors
- ✅ Values persist in database
- ✅ Refresh shows updated values

---

## Troubleshooting

### "Access Denied" error

- Make sure you're using the correct MySQL password
- Default username is usually "root"

### "Database ATC_DB not found"

- Make sure your database is created first
- Run: `setup_database.bat` before applying fixes

### "Trigger already exists" error

- This is normal - the script drops the old one first
- Just means the fix is being applied

### Still getting runway assignment error

- Make sure backend server was restarted after applying fixes
- Clear browser cache and login again
- Check that apply_fixes.sql executed without errors

---

## Files Available

| File                      | Purpose                                |
| ------------------------- | -------------------------------------- |
| `apply_fixes.sql`         | SQL script with all fixes              |
| `apply_fixes.bat`         | Batch file runner (searches for MySQL) |
| `apply_fixes.ps1`         | PowerShell script runner               |
| `FIX_QUICK_REFERENCE.txt` | Quick reference guide                  |
| `RUNWAY_FIX_GUIDE.md`     | This detailed guide                    |

---

## Questions?

If you still have issues:

1. Check the error message carefully
2. Make sure MySQL is running (`services.msc`)
3. Verify your ATC_DB database exists
4. Try Method 1 (MySQL Workbench) - it's the most reliable

Good luck! 🎉
