-- RUNWAY ASSIGNMENT FIX - Copy & Paste SQL Commands
-- Use this if you want to manually run the fixes in MySQL Workbench

-- Step 1: Select the database
USE ATC_DB;

-- Step 2: Drop and recreate the fixed trigger
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

-- Step 3: Drop and recreate procedures
DROP PROCEDURE IF EXISTS AssignRunway;

DELIMITER //
CREATE PROCEDURE AssignRunway(
    IN p_flight_id INT,
    IN p_runway_id INT
)
BEGIN
    UPDATE flights
    SET runway_id = p_runway_id
    WHERE flight_id = p_flight_id;
END//
DELIMITER ;

-- Step 4: Verify the fixes were applied
SELECT 'Database fixes completed!' AS status;

-- Step 5: Check flights to see runway assignments
SELECT flight_id, status, runway_id, gate_id FROM flights LIMIT 5;

-- That's it! The fixes are now applied.
-- Next: Restart your backend server and test the runway assignment feature.
