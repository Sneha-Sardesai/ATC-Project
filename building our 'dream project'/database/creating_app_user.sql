CREATE USER 'atc_user'@'localhost' IDENTIFIED BY 'atc123';
GRANT ALL PRIVILEGES ON ATC_DB.* TO 'atc_user'@'localhost';
FLUSH PRIVILEGES;
