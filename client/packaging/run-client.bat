@echo off
setlocal
cd /d "%~dp0"

if not exist config.json (
    copy /Y config.example.json config.json >nul
    echo Created config.json from config.example.json. Edit it before connecting.
)

java -jar 2009scape-client.jar
