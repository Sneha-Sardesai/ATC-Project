USE ATC_DB;

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
-- ASSIGN CONTROLLER
-- =========================
DELIMITER //
CREATE PROCEDURE AssignController(
    IN p_flight_id INT,
    IN p_controller_id INT
)
BEGIN
    INSERT INTO assignments
    VALUES (p_flight_id, p_controller_id);
END//
DELIMITER ;

-- =========================
-- DECLARE EMERGENCY
-- =========================
DELIMITER //
CREATE PROCEDURE DeclareEmergency(
    IN p_flight_id INT,
    IN p_emergency_type VARCHAR(30),
    IN p_priority INT
)
BEGIN
    INSERT INTO emergencies (flight_id, emergency_type, priority)
    VALUES (p_flight_id, p_emergency_type, p_priority);

    UPDATE flights
    SET status = 'EMERGENCY'
    WHERE flight_id = p_flight_id;
END//
DELIMITER ;

-- =========================
-- ASSIGN RUNWAY
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
END//
DELIMITER ;

-- =========================
-- ASSIGN GATE
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
END//
DELIMITER ;

-- =========================
-- UPDATE FLIGHT STATUS
-- =========================
DELIMITER //
CREATE PROCEDURE UpdateFlightStatus(
    IN p_flight_id INT,
    IN p_status VARCHAR(20)
)
BEGIN
    UPDATE flights
    SET status = p_status
    WHERE flight_id = p_flight_id;
END//
DELIMITER ;