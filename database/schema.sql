DROP DATABASE IF EXISTS ATC_DB;
CREATE DATABASE ATC_DB;
USE ATC_DB;

-- =========================
-- ENUMS (STRICT CONTROL)
-- =========================
CREATE TABLE FLIGHT_STATUS_ENUM (
    status VARCHAR(30) PRIMARY KEY
);

INSERT INTO FLIGHT_STATUS_ENUM VALUES
('SCHEDULED'),
('APPROACHING'),
('HOLDING'),
('LANDED'),
('DEPARTED'),
('EMERGENCY');

CREATE TABLE EMERGENCY_TYPE_ENUM (
    type VARCHAR(30) PRIMARY KEY
);

INSERT INTO EMERGENCY_TYPE_ENUM VALUES
('MEDICAL'),
('ENGINE_FAILURE'),
('FUEL_LEAK'),
('WEATHER'),
('HIJACK');

-- =========================
-- CONTROLLER (AUTH READY)
-- =========================
CREATE TABLE CONTROLLER (
    controller_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'ATC',
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

-- =========================
-- AIRCRAFT
-- =========================
CREATE TABLE AIRCRAFT (
    aircraft_id INT PRIMARY KEY,
    model VARCHAR(50),
    capacity INT,
    type VARCHAR(30)
);

-- =========================
-- RUNWAY
-- =========================
CREATE TABLE RUNWAY (
    runway_id INT PRIMARY KEY,
    code VARCHAR(10),
    status VARCHAR(20)
);

-- =========================
-- GATE
-- =========================
CREATE TABLE GATE (
    gate_id INT PRIMARY KEY,
    gate_number VARCHAR(10),
    terminal VARCHAR(10),
    status VARCHAR(20)
);

-- =========================
-- FLIGHT (SYSTEM GENERATED)
-- =========================
CREATE TABLE FLIGHT (
    flight_id INT PRIMARY KEY,
    airline VARCHAR(50),
    status VARCHAR(30),
    scheduled_time DATETIME,
    actual_time DATETIME,
    aircraft_id INT,
    runway_id INT,
    gate_id INT,

    FOREIGN KEY (aircraft_id) REFERENCES AIRCRAFT(aircraft_id),
    FOREIGN KEY (runway_id) REFERENCES RUNWAY(runway_id),
    FOREIGN KEY (gate_id) REFERENCES GATE(gate_id),
    FOREIGN KEY (status) REFERENCES FLIGHT_STATUS_ENUM(status)
);

-- =========================
-- EMERGENCY FLIGHT
-- =========================
CREATE TABLE EMERGENCY_FLIGHT (
    flight_id INT PRIMARY KEY,
    emergency_type VARCHAR(30),
    priority_level INT CHECK (priority_level BETWEEN 1 AND 5),

    FOREIGN KEY (flight_id) REFERENCES FLIGHT(flight_id),
    FOREIGN KEY (emergency_type) REFERENCES EMERGENCY_TYPE_ENUM(type)
);

-- =========================
-- FLIGHT ASSIGNMENT
-- =========================
CREATE TABLE FLIGHT_CONTROLLER_ASSIGNMENT (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_id INT,
    controller_id INT,
    assigned_at DATETIME DEFAULT NOW(),

    FOREIGN KEY (flight_id) REFERENCES FLIGHT(flight_id),
    FOREIGN KEY (controller_id) REFERENCES CONTROLLER(controller_id)
);

-- =========================
-- STATUS LOG
-- =========================
CREATE TABLE FLIGHT_STATUS_LOG (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_id INT,
    old_status VARCHAR(30),
    new_status VARCHAR(30),
    changed_at DATETIME DEFAULT NOW(),

    FOREIGN KEY (flight_id) REFERENCES FLIGHT(flight_id)
);