DELIMITER $$

CREATE TRIGGER trg_emergency_status
AFTER INSERT ON EMERGENCY_FLIGHT
FOR EACH ROW
BEGIN
    UPDATE FLIGHT
    SET Current_Status = 'EMERGENCY'
    WHERE Flight_ID = NEW.Flight_ID;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER trg_block_assignment_emergency
BEFORE UPDATE ON FLIGHT
FOR EACH ROW
BEGIN
    IF OLD.Current_Status = 'EMERGENCY' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot reassign runway/gate during emergency';
    END IF;
END$$

DELIMITER ;