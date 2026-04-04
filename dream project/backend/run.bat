@echo off
javac -d bin -cp "src;lib/*" src/service/*.java src/dao/*.java src/db/*.java src/model/*.java src/scheduler/*.java src/api/*.java src/util/*.java src/app/*.java
if %errorlevel% neq 0 (
    echo Compilation Failed
    exit /b %errorlevel%
)
echo Compilation Successful
java -cp "bin;lib/*" app.App
