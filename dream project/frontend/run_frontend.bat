@echo off
javac -d bin -cp "../backend/src;../lib/*" ATCFrontend.java
if %errorlevel% neq 0 (
    echo Compilation Failed
    exit /b %errorlevel%
)
echo Compilation Successful
java -cp "bin;../backend/src;../lib/*" frontend.ATCFrontend