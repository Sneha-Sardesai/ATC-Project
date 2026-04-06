# Runway Assignment Fix - Setup Instructions

## Issues Fixed

### 1. **Trigger Preventing Runway Updates** ❌ → ✓

**Problem**: The trigger `trg_gate_only_after_landing` was being triggered on ANY update to the flights table, including runway_id updates. Even though we were only updating runway_id, the trigger would check if gate_id was assigned, and if the flight status wasn't LANDED or EMERGENCY, it would throw an error.

**Solution**: Modified the trigger to only validate gate assignment constraints when `gate_id` is actually being changed:

```sql
IF NEW.gate_id IS NOT NULL
    AND (OLD.gate_id IS NULL OR NEW.gate_id != OLD.gate_id)
    AND OLD.status NOT IN ('LANDED', 'EMERGENCY') THEN
```

### 2. **Database Persistence** ❌ → ✓

**Problem**: The `AssignRunway` stored procedure was already using UPDATE, but the trigger error was preventing it from executing successfully.

**Solution**: Procedures were already correct. With the trigger fix, UPDATE statements will now execute successfully.

### 3. **Frontend Refresh**

The frontend already correctly:

- Calls the API to update runway
- Updates the UI immediately
- Refreshes the flight table
- The Refresh button in the details dialog re-fetches from server

## How to Apply Fixes

### Step 1: Update Database

Run this script in MySQL:

```bash
mysql -u root -p ATC_DB < apply_fixes.sql
```

Or manually execute these SQL commands in MySQL Workbench:

**Drop and Recreate the Fixed Trigger:**

```sql
DROP TRIGGER IF EXISTS trg_gate_only_after_landing;

DELIMITER //
CREATE TRIGGER trg_gate_only_after_landing
BEFORE UPDATE ON flights
FOR EACH ROW
BEGIN
    -- Only validate if gate_id is being changed to a non-NULL value
    IF NEW.gate_id IS NOT NULL
        AND (OLD.gate_id IS NULL OR NEW.gate_id != OLD.gate_id)
        AND OLD.status NOT IN ('LANDED', 'EMERGENCY') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Gate can only be assigned after landing or during emergency';
    END IF;
END//
DELIMITER ;
```

**Verify Changes:**

```sql
SELECT flight_id, status, runway_id, gate_id FROM flights LIMIT 5;
```

### Step 2: Recompile Backend (if needed)

```bash
cd backend
javac -d bin/api src/api/SimpleServer.java 2>&1
```

### Step 3: Restart Backend Server

```bash
cd backend
java -cp bin:lib/* api.SimpleServer
```

## How It Works Now

1. **User clicks "View Details"** on a flight
2. **Dialog shows flight details with 3 buttons:**
   - Assign Runway
   - Refresh
   - Close

3. **User clicks "Assign Runway":**
   - Input dialog: Enter Runway ID
   - Frontend sends POST to `/flights/runway` with `{flightId, runwayId}`
   - Backend calls `AssignRunway` stored procedure
   - Procedure executes: `UPDATE flights SET runway_id = ? WHERE flight_id = ?`
   - **Trigger checks ONLY if gate_id is changing** (it's not) → No error
   - Database UPDATE succeeds ✓
   - Frontend shows success message

4. **UI Updates:**
   - Dialog immediately shows new runway number
   - Main flight table refreshes (no refresh button needed)
   - Consistent data across the application

5. **User clicks "Refresh" button (optional):**
   - Dialog re-fetches flight details from server
   - Shows latest data from database

## Files Modified

1. **database/triggers.sql** - Fixed trigger condition
2. **database/fix_procedures.sql** - Added explicit AssignRunway and AssignGate procedures
3. **database/apply_fixes.sql** - New script to apply all fixes
4. **frontend/ATCSystemGUI.java** - Already correctly implemented (no changes needed)
5. **backend/src/dao/FlightDAO.java** - Already correctly implemented (no changes needed)

## Testing

After applying fixes, test the flow:

```
1. Login to frontend
2. Select a flight and click "View Details"
3. Click "Assign Runway"
4. Enter runway ID (e.g., 1, 2, 3)
5. Should see success message
6. Should see runway updated in dialog
7. Click "Refresh" to verify database persistence
8. Should still see the runway number
```

## Verification

Check database contains the updated runway:

```sql
SELECT flight_id, status, runway_id, gate_id FROM flights WHERE flight_id = <your_flight_id>;
```

The runway_id column should show the assigned runway number.
