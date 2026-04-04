@echo off
echo Setting up ATC_DB database...

REM Assuming MySQL is installed in default location
REM Adjust path if MySQL is installed elsewhere

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if not exist %MYSQL_PATH% (
    echo MySQL not found at %MYSQL_PATH%. Please install MySQL or adjust the path in this script.
    echo You can download MySQL from https://dev.mysql.com/downloads/mysql/
    pause
    exit /b 1
)

%MYSQL_PATH% -u root -pOpenEveryDoor2386* < schema.sql
if %errorlevel% neq 0 (
    echo Failed to run schema.sql
    exit /b %errorlevel%
)

%MYSQL_PATH% -u root -pOpenEveryDoor2386* < enums.sql
if %errorlevel% neq 0 (
    echo Failed to run enums.sql
    exit /b %errorlevel%
)

%MYSQL_PATH% -u root -pOpenEveryDoor2386* < procedures.sql
if %errorlevel% neq 0 (
    echo Failed to run procedures.sql
    exit /b %errorlevel%
)

%MYSQL_PATH% -u root -pOpenEveryDoor2386* < triggers.sql
if %errorlevel% neq 0 (
    echo Failed to run triggers.sql
    exit /b %errorlevel%
)

%MYSQL_PATH% -u root -pOpenEveryDoor2386* < seed.sql
if %errorlevel% neq 0 (
    echo Failed to run seed.sql
    exit /b %errorlevel%
)

echo Database setup complete!