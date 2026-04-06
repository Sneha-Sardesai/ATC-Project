USE ATC_DB;

DROP PROCEDURE IF EXISTS AddFlight;
DROP PROCEDURE IF EXISTS AssignRunway;
DROP PROCEDURE IF EXISTS AssignGate;

-- =========================
-- ADD FLIGHT (SYSTEM)
-- =========================
DELIMITER //
CREATE PROCEDURE AddFlight(
    IN p_flight_id INT,
    IN p_status VARCHAR(20),
    IN p_aircraft_id INT,
    IN p_runway_id INT,
    IN p_gate_id INT
)
BEGIN
    INSERT INTO flights
    VALUES (p_flight_id, p_aircraft_id, p_status, p_runway_id, p_gate_id);
END//
DELIMITER ;

-- =========================
-- ASSIGN RUNWAY (UPDATE)
-- =========================
DELIMITER //
CREATE PROCEDURE AssignRunway(
    IN p_flight_id INT,
    IN p_runway_id INT
)
BEGIN
    UPDATE flights
    SET runway_id = p_runway_id
    WHERE flight_id = p_flight_id;
    
    SELECT ROW_COUNT() AS updated_rows;
END//
DELIMITER ;

-- =========================
-- ASSIGN GATE (UPDATE)
-- =========================
DELIMITER //
CREATE PROCEDURE AssignGate(
    IN p_flight_id INT,
    IN p_gate_id INT
)
BEGIN
    UPDATE flights
    SET gate_id = p_gate_id,
        status = 'GATE_ASSIGNED'
    WHERE flight_id = p_flight_id;
    
    SELECT ROW_COUNT() AS updated_rows;
END//
DELIMITER ;