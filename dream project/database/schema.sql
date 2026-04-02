DROP DATABASE IF EXISTS ATC_DB;
CREATE DATABASE ATC_DB;
USE ATC_DB;

-- =========================
-- CONTROLLERS
-- =========================
CREATE TABLE controllers (
    controller_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (controller_id)
) ENGINE=InnoDB;

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

-- Ensure controller_id is auto-increment and compatible with FK references in assignments
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE controllers MODIFY controller_id INT NOT NULL AUTO_INCREMENT;
SET FOREIGN_KEY_CHECKS = 1;

-- Backfill passwords on seed data
INSERT INTO controllers (controller_id, name, password) VALUES
    (1, 'Amit', 'password1'),
    (2, 'Neha', 'password2'),
    (3, 'Rahul', 'password3')
ON DUPLICATE KEY UPDATE password = VALUES(password);
