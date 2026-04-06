# Quick Fix for Database Updates - Choose Your Method

The batch file had path parsing issues. Here are the fastest ways to apply the fixes:

---

## ⚡ **QUICKEST SOLUTION (2 minutes)**

### **Use MySQL Workbench**

1. **Open MySQL Workbench**
   - Search for "MySQL Workbench" in Start Menu
   - Double-click to open

2. **File → Open SQL Script**
   - Look for the menu at the top
   - Click **File** → **Open SQL Script**
   - Navigate to: `dream project/database/apply_fixes.sql`
   - Click "Open"

3. **Execute**
   - See the SQL code in the editor
   - Click the **Lightning ⚡ icon** at the top toolbar
   - Or press **Ctrl+Enter**

4. **Done!** ✓
   - You should see: `Rows affected: 0` or success message
   - Restart backend server to test

---

## **Alternative: Copy-Paste SQL (1 minute)**

If you prefer to manually copy-paste:

1. **Open file**: `dream project/database/MANUAL_SQL_COMMANDS.sql`
2. **Select all** (Ctrl+A)
3. **Copy** (Ctrl+C)
4. **In MySQL Workbench**: **File → New Query Tab**
5. **Paste** (Ctrl+V)
6. **Execute** (Ctrl+Enter or Lightning icon)
7. **Done!** ✓

---

## **Batch File (Fixed Version)**

I've fixed the batch file. You can try it again:

```bash
database\apply_fixes.bat
```

If it still has issues, you should get a message pointing you to use MySQL Workbench instead.

---

## **PowerShell (Optional)**

If you prefer PowerShell:

1. **Right-click Start Menu** → **Windows PowerShell (Admin)**
2. **Navigate to database folder:**
   ```powershell
   cd "path\to\database"
   ```
3. **Run:**
   ```powershell
   .\apply_fixes.ps1
   ```
4. **Enter credentials when prompted**
5. **Done!** ✓

---

## **After Applying Fixes**

1. **Restart backend server:**

   ```bash
   cd backend
   java -cp bin:lib/* api.SimpleServer
   ```

2. **Test in frontend:**
   - Login to frontend
   - Select a flight
   - Click "View Details"
   - Click "Assign Runway"
   - Enter runway ID (e.g., 1, 2, 3)
   - Should work! ✓

---

## **Recommendation**

👉 **Use MySQL Workbench** - It's the most reliably way that always works.

Takes only 2 minutes and has no compatibility issues.

Just: Open Workbench → File → Open SQL Script → select `apply_fixes.sql` → Click Lightning icon ⚡ → Done!

Good luck! 🚀
