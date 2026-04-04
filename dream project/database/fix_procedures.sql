USE ATC_DB;

DROP PROCEDURE IF EXISTS AddFlight;

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