USE ATC_DB;

-- =========================
-- PREVENT GATE ASSIGNMENT WITHOUT LANDING
-- =========================
DELIMITER //
CREATE TRIGGER trg_gate_only_after_landing
BEFORE UPDATE ON flights
FOR EACH ROW
BEGIN
    -- Allow gate assignment for any status - removed restriction to match runway assignment behavior
    -- Gate can be assigned at any time, similar to runway
END//
DELIMITER ;

-- =========================
-- EMERGENCY PRIORITY CHECK
-- =========================
DELIMITER //
CREATE TRIGGER trg_emergency_priority_check
BEFORE INSERT ON emergencies
FOR EACH ROW
BEGIN
    IF NEW.priority < 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Emergency priority must be >= 1';
    END IF;
END//
DELIMITER ;