# PowerShell script to apply runway assignment fixes
# This script finds MySQL and executes apply_fixes.sql

Write-Host ""
Write-Host "========================================"
Write-Host "Applying Runway Assignment Fixes"
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Search for MySQL installation
$mysqlPaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe",
    "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files (x86)\MySQL\MySQL Server 5.7\bin\mysql.exe"
)

$mysqlFound = $null

# Check each common path
foreach ($path in $mysqlPaths) {
    if (Test-Path $path) {
        $mysqlFound = $path
        Write-Host "Found MySQL at: $path" -ForegroundColor Green
        break
    }
}

# If not found in common paths, try PATH environment variable
if ($null -eq $mysqlFound) {
    try {
        $mysqlFound = (Get-Command mysql.exe -ErrorAction Stop).Source
        Write-Host "Found MySQL in PATH: $mysqlFound" -ForegroundColor Green
    }
    catch {
        Write-Host ""
        Write-Host "ERROR: MySQL not found!" -ForegroundColor Red
        Write-Host ""
        Write-Host "ALTERNATIVE METHODS:" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Option 1: Use MySQL Workbench (Easiest)" -ForegroundColor Cyan
        Write-Host "  1. Open MySQL Workbench"
        Write-Host "  2. Click 'File' menu"
        Write-Host "  3. Select 'Open SQL Script'"
        Write-Host "  4. Navigate to: database\apply_fixes.sql"
        Write-Host "  5. Click 'Execute' button or press Ctrl+Enter"
        Write-Host ""
        Write-Host "Option 2: Add MySQL to PATH" -ForegroundColor Cyan
        Write-Host "  1. Open 'Environment Variables' on your system"
        Write-Host "  2. Add MySQL bin folder to your PATH"
        Write-Host "  3. Run this script again"
        Write-Host ""
        Write-Host "Option 3: Run batch file with explicit path" -ForegroundColor Cyan
        Write-Host "  Edit apply_fixes.bat and set MYSQL_PATH manually"
        Write-Host ""
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Get the script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$sqlFile = Join-Path $scriptDir "apply_fixes.sql"

if (-not (Test-Path $sqlFile)) {
    Write-Host "ERROR: apply_fixes.sql not found at $sqlFile" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Using SQL file: $sqlFile" -ForegroundColor Cyan
Write-Host ""
Write-Host "Enter your MySQL credentials:" -ForegroundColor Yellow
$username = Read-Host "Username (default: root)"
if ([string]::IsNullOrEmpty($username)) {
    $username = "root"
}

$password = Read-Host "Password" -AsSecureString
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto([System.Runtime.InteropServices.Marshal]::SecureStringToCoTaskMemUnicode($password))

Write-Host ""
Write-Host "Executing apply_fixes.sql..." -ForegroundColor Cyan

# Execute the SQL file
try {
    & $mysqlFound -u $username -p$plainPassword ATC_DB < $sqlFile
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "========================================"
        Write-Host "SUCCESS: Database fixes applied!" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Next steps:" -ForegroundColor Yellow
        Write-Host "1. Restart the ATC Backend Server"
        Write-Host "2. Test the runway assignment feature"
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "ERROR: Failed to apply database fixes" -ForegroundColor Red
        Write-Host "Exit code: $LASTEXITCODE"
        Write-Host ""
    }
}
catch {
    Write-Host ""
    Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Write-Host "Press Enter to exit..." -ForegroundColor Gray
Read-Host
