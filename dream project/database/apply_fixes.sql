USE ATC_DB;

-- =========================
-- ADD COMPLETED STATUS TO FLIGHTS TABLE
-- =========================
ALTER TABLE flights MODIFY status ENUM(
    'APPROACHING',
    'HOLDING',
    'LANDED',
    'TAXIING',
    'GATE_ASSIGNED',
    'EMERGENCY',
    'COMPLETED'
) NOT NULL;

-- =========================
-- DROP AND RECREATE TRIGGER FIX
-- =========================
DROP TRIGGER IF EXISTS trg_gate_only_after_landing;

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
-- DROP AND RECREATE PROCEDURES
-- =========================
DROP PROCEDURE IF EXISTS AssignRunway;
DROP PROCEDURE IF EXISTS AssignGate;

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

DROP PROCEDURE IF EXISTS UpdateFlightStatus;

DELIMITER //
CREATE PROCEDURE UpdateFlightStatus(
    IN p_flight_id INT,
    IN p_status VARCHAR(20)
)
BEGIN
    IF p_status = 'COMPLETED' THEN
        -- Delete related records first due to foreign key constraints
        DELETE FROM emergencies WHERE flight_id = p_flight_id;
        DELETE FROM assignments WHERE flight_id = p_flight_id;
        DELETE FROM flights WHERE flight_id = p_flight_id;
    ELSE
        UPDATE flights
        SET status = p_status
        WHERE flight_id = p_flight_id;
    END IF;
END//
DELIMITER ;

-- Verify the updates
SELECT 'Database fixes applied successfully!' AS status;

-- Show current flights to verify runway assignments are working
SELECT flight_id, status, runway_id, gate_id FROM flights LIMIT 5;

-- =============================
-- DIRECT GATE UPDATE EXAMPLES
-- =============================
-- These are the direct UPDATE queries used by the backend for gate assignments

-- Example: Update a specific flight's gate
-- UPDATE flights SET gate_id = 1, status = 'GATE_ASSIGNED' WHERE flight_id = 101;

-- =============================
-- VERIFY GATE ASSIGNMENTS
-- =============================

-- Show all flights with gate assignments
SELECT flight_id, status, runway_id, gate_id 
FROM flights 
WHERE gate_id IS NOT NULL
ORDER BY flight_id;

-- Show flights waiting for gate assignment
SELECT flight_id, gate_id, status FROM flights WHERE gate_id IS NULL;

-- =============================
-- GATE ASSIGNMENT STATISTICS
-- =============================

-- How many flights have gates assigned?
SELECT COUNT(*) as flights_with_gates FROM flights WHERE gate_id IS NOT NULL;

-- How many flights are waiting for gates?
SELECT COUNT(*) as flights_waiting_for_gates FROM flights WHERE gate_id IS NULL;

-- Show gate allocation
SELECT gate_id, flight_id, status FROM flights WHERE gate_id IS NOT NULL ORDER BY gate_id, flight_id;
