USE ATC_DB;

-- =========================
-- PREVENT GATE ASSIGNMENT WITHOUT LANDING
-- =========================
DELIMITER //
CREATE TRIGGER trg_gate_only_after_landing
BEFORE UPDATE ON flights
FOR EACH ROW
BEGIN
    IF NEW.gate_id IS NOT NULL AND OLD.status NOT IN ('LANDED', 'EMERGENCY') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Gate can only be assigned after landing or during emergency';
    END IF;
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