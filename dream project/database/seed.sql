USE ATC_DB;

-- Controllers
INSERT INTO controllers VALUES
(1, 'Amit', 'password1'),
(2, 'Neha', 'password2'),
(3, 'Rahul', 'password3'),
(4, 'Kerris', '123456');

-- Aircrafts
INSERT INTO aircraft VALUES
(101, 'Boeing 737', 150, 'Commercial'),
(102, 'Airbus A320', 180, 'Commercial'),
(103, 'Cessna 172', 4, 'Private');

-- Optional: preloaded flights (system can also add later)
INSERT INTO flights VALUES
(500, 101, 'APPROACHING', NULL, NULL),
(501, 102, 'APPROACHING', NULL, NULL);

-- Assign initial controller
INSERT INTO assignments VALUES
(500, 1),
(501, 1);