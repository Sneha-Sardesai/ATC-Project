USE ATC_DB;

-- Auto-log status changes
DELIMITER $$

CREATE TRIGGER log_flight_status_change
AFTER UPDATE ON FLIGHT
FOR EACH ROW
BEGIN
    IF OLD.status <> NEW.status THEN
        INSERT INTO FLIGHT_STATUS_LOG (flight_id, old_status, new_status)
        VALUES (OLD.flight_id, OLD.status, NEW.status);
    END IF;
END $$

-- Auto-set flight status to EMERGENCY
CREATE TRIGGER set_emergency_status
AFTER INSERT ON EMERGENCY_FLIGHT
FOR EACH ROW
BEGIN
    UPDATE FLIGHT
    SET status = 'EMERGENCY'
    WHERE flight_id = NEW.flight_id;
END $$

DELIMITER ;