USE ATC_DB;

-- =====================================
-- LOOKUP TABLES (ENUM-LIKE DATA)
-- =====================================

-- Flight Status Types
INSERT INTO FLIGHT_STATUS_TYPE (Status_Code) VALUES
('APPROACHING'),
('LANDED'),
('TAXIING'),
('AT_GATE'),
('DEPARTED'),
('EMERGENCY');

-- Emergency Types
INSERT INTO EMERGENCY_TYPE (Emergency_Code) VALUES
('ENGINE_FAILURE'),
('MEDICAL'),
('LOW_FUEL'),
('HIJACK'),
('WEATHER');

-- =====================================
-- CONTROLLERS
-- =====================================
INSERT INTO CONTROLLER (Controller_ID, Name, Level, Status) VALUES
(1, 'Amit', 'Senior', 'Active'),
(2, 'Neha', 'Junior', 'Active');

-- =====================================
-- AIRCRAFT
-- =====================================
INSERT INTO AIRCRAFT (Aircraft_ID, Model, Capacity, Type) VALUES
(1, 'Boeing 737', 180, 'Passenger'),
(2, 'Airbus A320', 160, 'Passenger');

-- =====================================
-- RUNWAYS
-- =====================================
INSERT INTO RUNWAY (Runway_ID, Runway_Name, Runway_Status) VALUES
(1, 'RW1', 'AVAILABLE'),
(2, 'RW2', 'AVAILABLE');

-- =====================================
-- GATES
-- =====================================
INSERT INTO GATE (Gate_ID, Terminal, Gate_Status) VALUES
(1, 'T1', 'AVAILABLE'),
(2, 'T1', 'AVAILABLE'),
(3, 'T2', 'AVAILABLE');

-- =====================================
-- SYSTEM-CREATED FLIGHTS
-- (Controller does NOT type these)
-- =====================================
INSERT INTO FLIGHT
(Flight_ID, Aircraft_ID, Current_Status, Scheduled_Time, Actual_Time, Runway_ID, Gate_ID)
VALUES
(501, 1, 'APPROACHING', NOW(), NULL, NULL, NULL),
(502, 2, 'APPROACHING', NOW(), NULL, NULL, NULL);

-- =====================================
-- CONTROLLER ASSIGNMENTS
-- =====================================
INSERT INTO FLIGHT_CONTROLLER_ASSIGNMENT
(Assignment_ID, Flight_ID, Controller_ID, Assigned_At)
VALUES
(1, 501, 1, NOW()),
(2, 502, 2, NOW());

-- =====================================
-- EMERGENCY (OPTIONAL SEED)
-- =====================================
INSERT INTO EMERGENCY_FLIGHT
(Flight_ID, Emergency_Type, Priority_Level)
VALUES
(501, 'ENGINE_FAILURE', 1);