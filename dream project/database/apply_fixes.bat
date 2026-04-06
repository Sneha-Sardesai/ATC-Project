@echo off
REM Apply database fixes for runway assignment
REM Simple and robust version

setlocal enabledelayedexpansion

echo.
echo ========================================
echo Applying Runway Assignment Fixes
echo ========================================
echo.

REM Try to find mysql.exe in common locations
set MYSQL_EXE=

REM Check common installation paths
for %%P in (
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
    "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe"
    "C:\Program Files (x86)\MySQL\MySQL Server 5.7\bin\mysql.exe"
) do (
    if exist %%P (
        set MYSQL_EXE=%%P
        goto found
    )
)

:notfound
echo ERROR: MySQL executable not found in common locations
echo.
echo MySQL was not found. Here are your options:
echo.
echo [RECOMMENDED] Option 1: Use MySQL Workbench
echo   1. Open MySQL Workbench
echo   2. File menu ^> Open SQL Script
echo   3. Select: database\apply_fixes.sql
echo   4. Click Execute (Lightning icon) or Ctrl+Enter
echo.
echo Option 2: Use PowerShell script
echo   1. Open PowerShell
echo   2. Run: .\apply_fixes.ps1
echo.
echo Option 3: Manually run SQL commands
echo   1. Copy commands from: database\MANUAL_SQL_COMMANDS.sql
echo   2. Paste into MySQL Workbench
echo   3. Execute
echo.
pause
exit /b 1

:found
echo Found MySQL at: !MYSQL_EXE!
echo.
echo Executing apply_fixes.sql...
echo.

REM Execute the SQL file
"!MYSQL_EXE!" -u root -p ATC_DB < apply_fixes.sql

if !errorlevel! equ 0 (
    echo.
    echo ========================================
    echo SUCCESS: Database fixes applied!
    echo ========================================
    echo.
    echo Next steps:
    echo 1. Restart the ATC Backend Server
    echo 2. Test the runway assignment feature
    echo.
    pause
    exit /b 0
) else (
    echo.
    echo ERROR: Failed to apply database fixes
    echo.
    echo This could be due to:
    echo - Wrong MySQL password
    echo - MySQL server not running
    echo - Database ATC_DB does not exist
    echo.
    echo Try one of the alternative methods from the error above.
    echo.
    pause
    exit /b 1
)
    echo.
    echo Manual fix instructions:
    echo 1. Open MySQL Workbench
    echo 2. Connect to ATC_DB database
    echo 3. Open and run: database/apply_fixes.sql
    echo 4. Restart the backend server
    echo.
    pause
    exit /b 1
)
