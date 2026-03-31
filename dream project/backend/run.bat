@echo off
javac -d bin -cp "src;lib/*" src/app/App.java src/simulator/FlightSimulator.java src/service/*.java src/dao/*.java src/db/*.java src/model/*.java src/scheduler/*.java
if %errorlevel% neq 0 (
    echo Compilation Failed
    exit /b %errorlevel%
)
echo Compilation Successful
java -cp "bin;lib/*" app.App
