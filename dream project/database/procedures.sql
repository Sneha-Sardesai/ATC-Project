USE ATC_DB;

-- Change delimiter for procedures
DELIMITER $$

-- =========================================
-- 1. DECLARE EMERGENCY
-- =========================================
CREATE PROCEDURE DeclareEmergency(
    IN p_Flight_ID INT,
    IN p_Emergency_Type VARCHAR(50),
    IN p_Priority_Level INT
)
BEGIN
    INSERT INTO EMERGENCY_FLIGHT
    VALUES (p_Flight_ID, p_Emergency_Type, p_Priority_Level);
END $$

-- =========================================
-- 2. GET ALL EMERGENCY FLIGHTS
-- =========================================
CREATE PROCEDURE GetEmergencyFlights()
BEGIN
    SELECT f.Flight_ID, e.Emergency_Type, e.Priority_Level
    FROM FLIGHT f
    JOIN EMERGENCY_FLIGHT e ON f.Flight_ID = e.Flight_ID
    ORDER BY e.Priority_Level ASC;
END $$

-- =========================================
-- 3. ASSIGN RUNWAY
-- =========================================
CREATE PROCEDURE AssignRunway(
    IN p_Flight_ID INT,
    IN p_Runway_ID INT
)
BEGIN
    UPDATE FLIGHT
    SET Runway_ID = p_Runway_ID
    WHERE Flight_ID = p_Flight_ID;
END $$

-- =========================================
-- 4. ASSIGN GATE
-- =========================================
CREATE PROCEDURE AssignGate(
    IN p_Flight_ID INT,
    IN p_Gate_ID INT
)
BEGIN
    UPDATE FLIGHT
    SET Gate_ID = p_Gate_ID
    WHERE Flight_ID = p_Flight_ID;
END $$

-- =========================================
-- 5. ASSIGN CONTROLLER
-- =========================================
CREATE PROCEDURE AssignController(
    IN p_Assignment_ID INT,
    IN p_Flight_ID INT,
    IN p_Controller_ID INT
)
BEGIN
    INSERT INTO FLIGHT_CONTROLLER_ASSIGNMENT
    VALUES (p_Assignment_ID, p_Flight_ID, p_Controller_ID);
END $$

-- =========================================
-- 6. UPDATE FLIGHT STATUS
-- =========================================
CREATE PROCEDURE UpdateFlightStatus(
    IN p_Flight_ID INT,
    IN p_Status VARCHAR(50)
)
BEGIN
    UPDATE FLIGHT
    SET Status = p_Status
    WHERE Flight_ID = p_Flight_ID;
END $$

-- =========================================
-- 7. ADD STATUS LOG
-- =========================================
CREATE PROCEDURE AddStatusLog(
    IN p_Log_ID INT,
    IN p_Flight_ID INT,
    IN p_Status VARCHAR(50)
)
BEGIN
    INSERT INTO FLIGHT_STATUS_LOG
    VALUES (p_Log_ID, p_Flight_ID, p_Status, NOW());
END $$

-- =========================================
-- 8. ADD NEW FLIGHT
-- =========================================
CREATE PROCEDURE AddFlight(
    IN p_Flight_ID INT,
    IN p_Status VARCHAR(50),
    IN p_Aircraft_ID INT,
    IN p_Runway_ID INT,
    IN p_Gate_ID INT
)
BEGIN
    INSERT INTO FLIGHT (Flight_ID, Status, Scheduled_Time, Actual_Time, Aircraft_ID, Runway_ID, Gate_ID)
    VALUES (p_Flight_ID, p_Status, NOW(), NULL, p_Aircraft_ID, p_Runway_ID, p_Gate_ID);
END $$

-- Reset delimiter
DELIMITER ;