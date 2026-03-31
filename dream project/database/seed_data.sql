INSERT INTO CONTROLLER VALUES
(1, 'Amit', 'Senior', 'Active'),
(2, 'Neha', 'Junior', 'Active');

INSERT INTO AIRCRAFT VALUES
(1, 'Boeing 737', 180, 'Passenger');

INSERT INTO RUNWAY VALUES
(1, 'RW1', 'Available');

INSERT INTO GATE VALUES
(1, 'G1', 'T1', 'Available');

CALL AddFlight(101, 1);

INSERT INTO FLIGHT_CONTROLLER_ASSIGNMENT
VALUES (1, 101, 1, NOW());