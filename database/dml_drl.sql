USE ATC_DB;

-- DML
CALL DeclareEmergency(301, 'Medical', 1);
SELECT * FROM EMERGENCY_FLIGHT;

CALL AssignRunway(301, 1);
SELECT Flight_ID, Runway_ID FROM FLIGHT;

CALL UpdateFlightStatus(301, 'Landing');
SELECT Flight_ID, Status FROM FLIGHT;

DELETE FROM EMERGENCY_FLIGHT WHERE Flight_ID = 301;
SELECT * FROM EMERGENCY_FLIGHT;

-- DRL
CALL GetEmergencyFlights();

SELECT f.Flight_ID, e.Emergency_Type, e.Priority_Level
FROM FLIGHT f
JOIN EMERGENCY_FLIGHT e ON f.Flight_ID = e.Flight_ID;

SELECT * FROM FLIGHT WHERE Status = 'Landing';