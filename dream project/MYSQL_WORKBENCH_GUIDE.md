# MySQL Workbench - Step by Step Guide

## What to Do in MySQL Workbench (Complete Guide)

---

## **Step 1: Open MySQL Workbench**

1. Click **Start Menu** (Windows logo at bottom-left)
2. Type: `mysql workbench`
3. Click **MySQL Workbench** when it appears
4. Wait for it to open (may take 10-15 seconds)

**You should now see the MySQL Workbench main window**

---

## **Step 2: Connect to Your Database**

You should see the Workbench home screen with available connections.

1. Look for **Local instance MySQL** (or similar)
2. **Double-click** on it to connect
3. If prompted for password, enter your MySQL password (usually just press Enter or type `root`)
4. Wait for connection to complete

**You're now connected to MySQL**

---

## **Step 3: Make Sure You're on the Right Database**

On the left side panel (Navigator):

1. You should see databases listed
2. Look for **ATC_DB** in the list
3. **Right-click** on **ATC_DB**
4. Click **Set as Default Database**
5. It should now be **bold** (highlighted)

**Good! Now you're working with the ATC_DB database**

---

## **Step 4: Open the SQL Fix Script**

At the top menu:

1. Click **File** menu
2. Select **Open SQL Script**
   - (You should see this option near the top)

**File browser window opens**

3. Navigate to your project folder:
   - Look for: `Dream Project`
   - → Open the `database` folder
   - Find file: `apply_fixes.sql`
   - Click it once to select it
   - Click **Open** button

**The SQL code now appears in the editor**

---

## **Step 5: Review the SQL Code**

In the editor (main area), you should see:

```sql
USE ATC_DB;

DROP TRIGGER IF EXISTS trg_gate_only_after_landing;

DELIMITER //
CREATE TRIGGER trg_gate_only_after_landing
...
```

This is the fix! It will:

- ✓ Fix the gate trigger
- ✓ Update runway assignment procedure
- ✓ Update gate assignment procedure

**Everything looks good**

---

## **Step 6: Execute the SQL**

Now run the SQL to apply fixes:

### **Method A: Lightning Icon (Easiest)**

1. Look at the top toolbar
2. Find the **Lightning ⚡ icon** (yellow lightning bolt)
3. **Click it** once
4. The script will execute

### **Method B: Keyboard Shortcut**

1. Press **Ctrl+Enter**
2. The script will execute

### **Method C: Menu**

1. Click **Query** menu
2. Click **Execute (All)** or **Execute Selection**

**Wait a few seconds for execution to complete**

---

## **Step 7: Check the Result**

Look at the **Output Panel** at the bottom:

### ✓ **SUCCESS** - You should see:

```
Database fixes applied successfully!
```

Or you might see:

```
0 rows affected
```

This is NORMAL and GOOD! It means:

- ✓ Trigger was dropped and recreated
- ✓ Procedures were updated
- ✓ No errors occurred

### ❌ **ERROR** - You might see:

```
Access Denied for user 'root'@'localhost'
```

This means:

- Wrong password - Try again with correct password
- Or click **Database** → **Manage Connections** to change password

---

## **Step 8: Verify the Changes**

Optional - You can check if the trigger was actually updated:

1. In SQL editor, do **Ctrl+A** to select all
2. Delete (or just create new tab)
3. Type this verification query:

```sql
SELECT * FROM information_schema.TRIGGERS
WHERE TRIGGER_NAME = 'trg_gate_only_after_landing'
AND TRIGGER_SCHEMA = 'ATC_DB';
```

4. Press **Ctrl+Enter** to execute
5. Look at results - if you see the trigger, it was created ✓

---

## **Step 9: Restart Your Backend Server**

Now the database is fixed! Restart the backend:

1. Open Command Prompt or PowerShell
2. Navigate to your project:

   ```bash
   cd "dream project\backend"
   ```

3. Start the server:

   ```bash
   java -cp bin:lib/* api.SimpleServer
   ```

4. Wait for it to say:
   ```
   ATC Server running on http://localhost:8080
   ```

**Backend is running!**

---

## **Step 10: Test the Feature**

Now test that everything works:

1. **Open the frontend** (run ATCSystemGUI.java)
2. **Login** with credentials
3. **View Flights**
4. **Click "View Details"** on a flight
5. **Click "Assign Runway"** button
6. **Enter a runway ID** (example: 1, 2, or 3)
7. Should see: **"Runway assigned successfully!"** ✓

8. **Click Refresh** button in the dialog
9. Should still show the runway number ✓

**Perfect! It's working!**

---

## **Troubleshooting**

### **Problem: Can't find apply_fixes.sql file**

- Make sure you're looking in correct folder
- Path should be: `dream project → database → apply_fixes.sql`
- Check file extension - make sure it says `.sql` not `.txt`

### **Problem: "Trigger already exists" error**

- This is FINE - the script drops it first
- Just means the fix is being applied
- Continue with the test

### **Problem: "Access Denied" error**

- Your MySQL password might be wrong
- Try: **Database → Manage Connections** to set correct password
- Or close Workbench and reopen

### **Problem: Can't see result**

- Scroll down in the Output panel at bottom
- Look for "Database fixes applied successfully!" message
- It might say "0 rows affected" which is also normal

### **Problem: Runway assignment still fails**

- Make sure you RESTARTED the backend server
- Reload the frontend
- Clear browser cache if using web frontend
- Try assigning runway again

---

## **Quick Checklist**

- [ ] MySQL Workbench is open
- [ ] Connected to Local instance MySQL
- [ ] ATC_DB is set as default database (bold in list)
- [ ] File → Open SQL Script → apply_fixes.sql is open
- [ ] SQL code is visible in editor
- [ ] Click Lightning icon ⚡ to execute
- [ ] See success message (or 0 rows affected)
- [ ] Restart backend server
- [ ] Test runway assignment in frontend
- [ ] ✓ Works!

---

## **Summary**

1. Open MySQL Workbench
2. Connect to Local instance
3. Set ATC_DB as default
4. File → Open SQL Script → apply_fixes.sql
5. Click Lightning icon ⚡
6. See success message
7. Restart backend
8. Test runway assignment
9. ✓ Done!

**Total time: 3-5 minutes**

Good luck! 🚀
