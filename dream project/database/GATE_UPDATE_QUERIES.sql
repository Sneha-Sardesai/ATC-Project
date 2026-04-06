-- Gate Assignment Update Queries
-- Use these to verify and update gate assignments in the database

USE ATC_DB;

-- =============================
-- DIRECT UPDATE GATE QUERY
-- =============================
-- This is the query used by the direct update method

-- Update a specific flight's gate
UPDATE flights 
SET gate_id = 1, status = 'GATE_ASSIGNED' 
WHERE flight_id = 101;

-- Verify it was updated
SELECT flight_id, gate_id, status FROM flights WHERE flight_id = 101;

-- =============================
-- VERIFY ALL GATE ASSIGNMENTS
-- =============================

-- Show all flights with gate assignments
SELECT flight_id, status, runway_id, gate_id 
FROM flights 
WHERE gate_id IS NOT NULL
ORDER BY flight_id;

-- =============================
-- UPDATE MULTIPLE GATE ASSIGNMENTS
-- =============================

-- Update gates for multiple flights (example)
UPDATE flights 
SET gate_id = CASE 
    WHEN flight_id = 101 THEN 1
    WHEN flight_id = 102 THEN 2
    WHEN flight_id = 103 THEN 3
    ELSE gate_id
END,
status = CASE
    WHEN flight_id IN (101, 102, 103) THEN 'GATE_ASSIGNED'
    ELSE status
END
WHERE flight_id IN (101, 102, 103);

-- =============================
-- CHECK GATE ASSIGNMENT STATUS
-- =============================

-- Show gates that are assigned
SELECT flight_id, gate_id, status FROM flights WHERE gate_id IS NOT NULL;

-- Show flights waiting for gate assignment
SELECT flight_id, gate_id, status FROM flights WHERE gate_id IS NULL;

-- =============================
-- RESET GATE ASSIGNMENT (if needed)
-- =============================

-- Clear all gate assignments
UPDATE flights SET gate_id = NULL WHERE 1=1;

-- Clear specific flight's gate
UPDATE flights SET gate_id = NULL WHERE flight_id = 101;

-- =============================
-- COUNT GATE ASSIGNMENTS
-- =============================

-- How many flights have gates assigned?
SELECT COUNT(*) as flights_with_gates FROM flights WHERE gate_id IS NOT NULL;

-- How many flights are waiting for gates?
SELECT COUNT(*) as flights_waiting_for_gates FROM flights WHERE gate_id IS NULL;

-- =============================
-- RECENT GATE ASSIGNMENTS
-- =============================

-- Show all gates and which flights are using them
SELECT gate_id, COUNT(*) as flight_count FROM flights WHERE gate_id IS NOT NULL GROUP BY gate_id;

-- Show gate allocation
SELECT gate_id, flight_id, status FROM flights WHERE gate_id IS NOT NULL ORDER BY gate_id, flight_id;
