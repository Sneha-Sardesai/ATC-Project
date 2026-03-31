USE ATC_DB;

DELIMITER $$

-- =========================================
-- 1. DECLARE EMERGENCY (Controller action)
-- =========================================
CREATE PROCEDURE DeclareEmergency(
    IN p_Flight_ID INT,
    IN p_Emergency_Type VARCHAR(30),
    IN p_Priority_Level INT
)
BEGIN
    -- Insert into emergency table
    INSERT INTO EMERGENCY_FLIGHT (Flight_ID, Emergency_Type, Priority_Level)
    VALUES (p_Flight_ID, p_Emergency_Type, p_Priority_Level);

    -- Status auto-update handled by trigger
END $$


-- =========================================
-- 2. GET EMERGENCY FLIGHTS (PRIORITIZED)
-- =========================================
CREATE PROCEDURE GetEmergencyFlights()
BEGIN
    SELECT f.Flight_ID,
           f.Current_Status,
           e.Emergency_Type,
           e.Priority_Level
    FROM FLIGHT f
    JOIN EMERGENCY_FLIGHT e
        ON f.Flight_ID = e.Flight_ID
    ORDER BY e.Priority_Level ASC;
END $$


-- =========================================
-- 3. GET FLIGHTS FOR CONTROLLER (IMPORTANT)
-- =========================================
CREATE PROCEDURE GetFlightsForController(
    IN p_Controller_ID INT
)
BEGIN
    SELECT f.Flight_ID,
           f.Current_Status,
           e.Priority_Level
    FROM FLIGHT f
    JOIN FLIGHT_CONTROLLER_ASSIGNMENT a
        ON f.Flight_ID = a.Flight_ID
    LEFT JOIN EMERGENCY_FLIGHT e
        ON f.Flight_ID = e.Flight_ID
    WHERE a.Controller_ID = p_Controller_ID
    ORDER BY
        CASE
            WHEN f.Current_Status = 'EMERGENCY' THEN 0
            ELSE 1
        END,
        e.Priority_Level ASC;
END $$


-- =========================================
-- 4. ASSIGN RUNWAY (Controller action)
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
-- 5. ASSIGN GATE (Controller action)
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
-- 6. ASSIGN CONTROLLER (System action)
-- =========================================
CREATE PROCEDURE AssignController(
    IN p_Flight_ID INT,
    IN p_Controller_ID INT
)
BEGIN
    INSERT INTO FLIGHT_CONTROLLER_ASSIGNMENT (Flight_ID, Controller_ID)
    VALUES (p_Flight_ID, p_Controller_ID);
END $$


-- =========================================
-- 7. UPDATE FLIGHT STATUS (Controlled)
-- =========================================
CREATE PROCEDURE UpdateFlightStatus(
    IN p_Flight_ID INT,
    IN p_Status VARCHAR(30)
)
BEGIN
    UPDATE FLIGHT
    SET Current_Status = p_Status
    WHERE Flight_ID = p_Flight_ID;
END $$


-- =========================================
-- 8. ADD NEW FLIGHT (SYSTEM ONLY)
-- =========================================
CREATE PROCEDURE AddFlight(
    IN p_Flight_ID INT,
    IN p_Aircraft_ID INT
)
BEGIN
    INSERT INTO FLIGHT (
        Flight_ID,
        Aircraft_ID,
        Current_Status,
        Scheduled_Time
    )
    VALUES (
        p_Flight_ID,
        p_Aircraft_ID,
        'APPROACHING',
        NOW()
    );
END $$


DELIMITER ;