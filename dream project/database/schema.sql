DROP DATABASE IF EXISTS ATC_DB;
CREATE DATABASE ATC_DB;
USE ATC_DB;

-- =========================
-- CONTROLLERS
-- =========================
CREATE TABLE controllers (
    controller_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- =========================
-- AIRCRAFTS
-- =========================
CREATE TABLE aircraft (
    Aircraft_ID INT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    type VARCHAR(30) NOT NULL
);

-- =========================
-- FLIGHTS
-- =========================
CREATE TABLE flights (
    flight_id INT PRIMARY KEY,
    aircraft_id INT NOT NULL,

    status ENUM(
        'APPROACHING',
        'HOLDING',
        'LANDED',
        'TAXIING',
        'GATE_ASSIGNED',
        'EMERGENCY'
    ) NOT NULL,

    runway_id INT NULL,
    gate_id INT NULL
);

-- =========================
-- FLIGHT ↔ CONTROLLER ASSIGNMENT
-- =========================
CREATE TABLE assignments (
    flight_id INT PRIMARY KEY,
    controller_id INT NOT NULL,

    FOREIGN KEY (flight_id) REFERENCES flights(flight_id),
    FOREIGN KEY (controller_id) REFERENCES controllers(controller_id)
);

-- =========================
-- EMERGENCIES
-- =========================
CREATE TABLE emergencies (
    emergency_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_id INT NOT NULL,

    emergency_type ENUM(
        'MEDICAL',
        'ENGINE_FAILURE',
        'LOW_FUEL',
        'HIJACK',
        'WEATHER'
    ) NOT NULL,

    priority INT NOT NULL,
    declared_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);