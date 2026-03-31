USE ATC_DB;

-- Controllers
INSERT INTO controllers VALUES
(1, 'Amit'),
(2, 'Neha'),
(3, 'Rahul');

-- Optional: preloaded flights (system can also add later)
INSERT INTO flights VALUES
(500, 101, 'APPROACHING', NULL, NULL),
(501, 102, 'APPROACHING', NULL, NULL);

-- Assign initial controller
INSERT INTO assignments VALUES
(500, 1),
(501, 1);